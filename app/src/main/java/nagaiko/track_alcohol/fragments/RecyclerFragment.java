package nagaiko.track_alcohol.fragments;

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

import nagaiko.track_alcohol.recyclerview.ClickRecyclerAdapter;
import nagaiko.track_alcohol.DataStorage;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class RecyclerFragment extends Fragment implements
        ClickRecyclerAdapter.OnItemClickListener{
    public static final String TAG = RecyclerFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";

    private static String[] names;
    private static String[] ingredient;
    private DataStorage dataStorage = DataStorage.getInstance();


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

        Cocktail[] data = (Cocktail[])dataStorage.getData(DataStorage.COCKTAIL_FILTERED_LIST);

        names = new String[data.length];
//        ingredient = new String[data.length];
        for (int i = 0; i < data.length; i++){
            names[i] = data[i].name;
//            ingredient[i] = data[i].categoryName;
        }

        recyclerView.setAdapter(new ClickRecyclerAdapter(getActivity().getLayoutInflater(),names, this));
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
        Toast.makeText(getActivity(), names[position], Toast.LENGTH_SHORT).show();
    }


}