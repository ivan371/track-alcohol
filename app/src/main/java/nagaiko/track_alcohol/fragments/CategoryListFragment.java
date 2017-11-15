package nagaiko.track_alcohol.fragments;

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

//import nagaiko.track_alcohol.DetailActivity;
import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.ListActivity;
import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.recyclerview.ClickCategoryListAdapter;
import nagaiko.track_alcohol.DataStorage;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class CategoryListFragment extends Fragment implements
        ClickCategoryListAdapter.OnItemClickListener{
    public static final String TAG = CategoryListFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";
    private static final String ID_COCKTAIL = "idCocktail";

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
        DBHelper db = new DBHelper(this.getActivity());
        categories = db.getCategories().toArray(new String[db.getCategories().size()]);

//        categories = new String[]{"Ordinary Drink", "Cocktail", "Milk / Float / Shake",
//            "Other/Unknown", "Cocoa", "Shot", "Coffee / Tea", "Homemade Liqueur",
//            "Punch / Party Drink", "Beer", "Soft Drink / Soda"};

//        data = db.getCategories();
//        categories = new String[data.size()];
//        for (int i=0; i<data.size(); i++) {
//            categories[i] = data.get(i);
//        }

        recyclerView.setAdapter(new ClickCategoryListAdapter(getActivity().getLayoutInflater(), categories, this));
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
        args.putString("category", categories[position]);
        cocktailListFragment.setArguments(args);
        fragmentTransaction = getFragmentManager().beginTransaction();
//        if (((ListActivity) getActivity()).isNetworkAvailable()) { // || db.getCocktailsByCategory(categories[position]) !=null) {
            fragmentTransaction.replace(R.id.fragment, cocktailListFragment);
//        } else {
//            Toast.makeText(getActivity(), "NO INTERNET", Toast.LENGTH_SHORT).show();
//        }
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