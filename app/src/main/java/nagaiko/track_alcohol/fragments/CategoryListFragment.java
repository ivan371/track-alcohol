package nagaiko.track_alcohol.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.recyclerview.ClickCategoryListAdapter;
import nagaiko.track_alcohol.DataStorage;

import static nagaiko.track_alcohol.api.ApiResponseTypes.CATEGORIES_LIST;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class CategoryListFragment extends Fragment implements
        ClickCategoryListAdapter.OnItemClickListener, DataStorage.Subscriber{
    public static final String TAG = CategoryListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ClickCategoryListAdapter recyclerAdapter;
    Toolbar toolbar;

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";
    private static final String ID_COCKTAIL = "idCocktail";

    private DataStorage dataStorage = DataStorage.getInstance();

    CocktailListFragment cocktailListFragment = new CocktailListFragment();
    FragmentTransaction fragmentTransaction;

    private List<String> categories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState");
            Log.d(TAG, Integer.toString(currentVisiblePosition));
            currentVisiblePosition = savedInstanceState.getInt(VISIBLE_POSITION);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        Log.d(TAG, Integer.toString(currentVisiblePosition));
        super.onSaveInstanceState(outState);
        outState.putInt(VISIBLE_POSITION, currentVisiblePosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());

//        categories = dataStorage.getCategories().toArray(new String[dataStorage.getCategories().size()]);
        dataStorage.getCategories(this);

        recyclerAdapter = new ClickCategoryListAdapter(getActivity().getLayoutInflater(), null, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        return recyclerView;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
    }

    @Override
    public void onItemClick(View view, int position) {

        DBHelper db = new DBHelper(this.getActivity());
        Bundle args = new Bundle();
        cocktailListFragment.setCategory(categories.get(position));
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, cocktailListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDataLoaded(int type, Response response) {
        if (type == CATEGORIES_LIST) {
            categories = (List<String>) response.content;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerAdapter.setNewData(categories);
                }
            });

        }
    }

    @Override
    public void onDataLoadFailed() {

    }
}