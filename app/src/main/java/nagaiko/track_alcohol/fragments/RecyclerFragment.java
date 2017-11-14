package nagaiko.track_alcohol.fragments;

import android.content.Intent;
import android.os.Bundle;
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

//import nagaiko.track_alcohol.DetailActivity;
import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.DetailActivity;
import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.recyclerview.ClickCocktailListAdapter;
import nagaiko.track_alcohol.recyclerview.ClickRecyclerAdapter;
import nagaiko.track_alcohol.DataStorage;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.recyclerview.ListRecyclerAdapter;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class RecyclerFragment extends Fragment implements
        ClickRecyclerAdapter.OnItemClickListener{
    public static final String TAG = RecyclerFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";
    private static final String ID_COCKTAIL = "idCocktail";

    private DBHelper db;
    private ArrayList<String> data;
    private DataStorage dataStorage = DataStorage.getInstance();

    CocktailListFragment cocktailListFragment = new CocktailListFragment();
    FragmentTransaction fragmentTransaction;

    private String[] categories;

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

//        data = dataStorage.getCocktailsByCategory("Ordinary Drink");

        data = db.getCategories();
        categories = new String[data.size()];
        for (int i=0; i<data.size(); i++) {
            categories[i] = data.get(i);
        }

        recyclerView.setAdapter(new ClickRecyclerAdapter(getActivity().getLayoutInflater(), categories, this));
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

        Bundle args = new Bundle();
        args.putString("category", categories[position]);
        cocktailListFragment.setArguments(args);
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment, cocktailListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

//        Toast.makeText(getActivity(), data.get(position).getName(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), DetailActivity.class);
//        intent.putExtra(ID_COCKTAIL, data.get(position).getId());

//        Toast.makeText(getActivity(), names[position], Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), DetailActivity.class);
//        intent.putExtra(ID_COCKTAIL, ids[position]);
//
//        startActivity(intent);
    }


}