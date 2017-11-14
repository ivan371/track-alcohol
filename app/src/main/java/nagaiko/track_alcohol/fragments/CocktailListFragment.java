package nagaiko.track_alcohol.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import nagaiko.track_alcohol.DetailActivity;
import nagaiko.track_alcohol.recyclerview.ClickCocktailListAdapter;
import nagaiko.track_alcohol.recyclerview.ClickRecyclerAdapter;
import nagaiko.track_alcohol.DataStorage;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class CocktailListFragment extends Fragment implements
        ClickCocktailListAdapter.OnItemClickListener{
    public static final String TAG = RecyclerFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";
    private static final String ID_COCKTAIL = "idCocktail";

    private static String[] names;
    private static int[] ids;
//private static Integer[] ids;
//    private static ArrayList<String> name;
//    private static ArrayList<Integer> id;
    private static String[] ingredient;
    private DataStorage dataStorage = DataStorage.getInstance();

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

        ArrayList<Cocktail> data = dataStorage.getCocktailsByCategory(category);

        recyclerView.setAdapter(new ClickCocktailListAdapter(getActivity().getLayoutInflater(), data, this));
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
//        Toast.makeText(getActivity(), names[position], Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(ID_COCKTAIL, ids[position]);

        startActivity(intent);
    }


}

////        Cocktail[] data = (Cocktail[])dataStorage.getData(DataStorage.COCKTAIL_FILTERED_LIST);
//
//    // НЕ РАБОТАЕТ ЕСЛИ ТАК
////        ListActivity la = (ListActivity) this.getActivity();
////        String[] data = la.getCategories().toArray(new String[la.getCategories().size()]);
//    //А ТАК РАБОТАЕТ
//    String[] data = new String[]{"Ordinary Drink", "Cocktail", "Milk / Float / Shake",
//            "Other/Unknown", "Cocoa", "Shot", "Coffee / Tea", "Homemade Liqueur",
//            "Punch / Party Drink", "Beer", "Soft Drink / Soda"};
//
//        names = new String[data.length];
//                ids = new int[data.length];
////        ingredient = new String[data.length];
//                for (int i = 0; i < data.length; i++){
//        names[i] = data[i];
////            ids[i] = data[i].getId();
////            ingredient[i] = data[i].categoryName;
//        }