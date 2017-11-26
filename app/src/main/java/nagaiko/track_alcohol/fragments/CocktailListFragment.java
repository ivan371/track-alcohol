package nagaiko.track_alcohol.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import nagaiko.track_alcohol.DBHelper;

import nagaiko.track_alcohol.DetailActivity;
import nagaiko.track_alcohol.ListActivity;
import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.recyclerview.ClickCocktailListAdapter;
import nagaiko.track_alcohol.DataStorage;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class CocktailListFragment extends Fragment implements
        ClickCocktailListAdapter.OnItemClickListener, DataStorage.Subscriber{
    public static final String TAG = CategoryListFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";
    private static final String ID_COCKTAIL = "idCocktail";

//    private static String[] names;
//    private static int[] ids;
//private static Integer[] ids;
//    private static ArrayList<String> name;
//    private static ArrayList<Integer> id;
    ArrayList<Cocktail> data;
    private static String[] ingredient;
    private DataStorage dataStorage = DataStorage.getInstance();
    private FragmentTransaction fragmentTransaction;
    private ClickCocktailListAdapter recyclerAdapter;

    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState");
            Log.d(TAG, Integer.toString(currentVisiblePosition));
            currentVisiblePosition = savedInstanceState.getInt(VISIBLE_POSITION);
        }
        category = getArguments().getString("category");
        dataStorage.subscribe(this);
//        Toast.makeText(getActivity(), category, Toast.LENGTH_SHORT).show();

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

        data = dataStorage.getCocktailsByCategory(category);

        recyclerAdapter = new ClickCocktailListAdapter(getActivity().getLayoutInflater(), data, this);
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
    public void onStop() {
        dataStorage.unsubscribe(this);
        super.onStop();
    }

    @Override
    public void onItemClick(View view, int position) {
//        Toast.makeText(getActivity(), names[position], Toast.LENGTH_SHORT).show();
        DBHelper db = new DBHelper(this.getActivity());
//        if (((ListActivity) getActivity()).isNetworkAvailable()) { // || db.getCocktailById(data.get(position).getId())!=null) {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(ID_COCKTAIL, data.get(position).getId());

            startActivity(intent);
//        } else {
//            Toast.makeText(getActivity(), "NO INTERNET", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onDataUpdated(int dataType) {
        fragmentTransaction = getFragmentManager().beginTransaction();
//        CocktailListFragment cocktailListFragment = new CocktailListFragment();
//        Bundle args = new Bundle();
//        args.putString("category", category);
//        cocktailListFragment.setArguments(args);
//        fragmentTransaction.replace(R.id.fragment, cocktailListFragment);
//        fragmentTransaction.commit();
        data = dataStorage.getCocktailsByCategory(category);
        fragmentTransaction.detach(this).attach(this).commit();
//        recyclerAdapter.setNewData(data);
    }

    @Override
    public void onDataUpdateFail() {
        Snackbar.make(this.getView(), R.string.no_internet, Toast.LENGTH_SHORT)
        .setAction(R.string.action, snackbarOnClickListener).show();
    }

    CocktailListFragment cocktailListFragment = this;
    View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction()
                    .detach(cocktailListFragment).attach(cocktailListFragment).commit();
//                    .replace(R.id.fragment, new CocktailListFragment()).commit();
//            Toast.makeText(getActivity(), "reloaded", Toast.LENGTH_SHORT).show();
        }
    };

}