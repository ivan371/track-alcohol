package nagaiko.track_alcohol;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;

import nagaiko.track_alcohol.fragments.ErrorFragment;
import nagaiko.track_alcohol.fragments.RecyclerFragment;
import nagaiko.track_alcohol.models.Cocktail;

public class ListActivity extends AppCompatActivity {

    public final String LOG_TAG = this.getClass().getSimpleName();

    private RecyclerFragment fragment;

//    int currentVisiblePosition = 0;
//    private static final String VISIBLE_POSITION = "position";
//
//    private RecyclerView recyclerView;
//
//    private static String[] names;
    private DataStorage dataStorage = DataStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
//        if (savedInstanceState != null) {
//            currentVisiblePosition = savedInstanceState.getInt(VISIBLE_POSITION);
//        }
//        Log.d(LOG_TAG, Integer.toString(currentVisiblePosition));

        Intent intent = getIntent();

        final FragmentManager fm = getSupportFragmentManager();

        if(dataStorage.getData(DataStorage.COCKTAIL_FILTERED_LIST) == null){
            if (savedInstanceState == null) {
                fm.beginTransaction().replace(R.id.fragment, fragment, ErrorFragment.TAG).commit();
            }
        } else {
            if (savedInstanceState == null) {
                fragment = new RecyclerFragment();
                fm.beginTransaction().replace(R.id.fragment, fragment, RecyclerFragment.TAG).commit();
            }else {
                fragment = (RecyclerFragment) getSupportFragmentManager().findFragmentByTag(RecyclerFragment.TAG);
            }
        }

//        recyclerView = new RecyclerView(this);
//
//        Cocktail[] data = (Cocktail[])dataStorage.getData(DataStorage.COCKTAIL_FILTERED_LIST);
//
//        names = new String[data.length];
//        for (int i = 0; i < data.length; i++){
//            names[i] = data[i].name;
//        }
//
//        recyclerView.setAdapter(new ClickRecyclerAdapter(getLayoutInflater(),names, this));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        recyclerView.setHasFixedSize(true);
//        setContentView(recyclerView);
    }

//    @Override
//    public void onItemClick(View view, int position) {
//        Toast.makeText(this, names[position], Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume");
//        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "onPause");

    }

    @Override
    protected void onStop(){
        super.onStop();

        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
//        Log.d(LOG_TAG, Integer.toString(currentVisiblePosition));

        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//        outState.putInt(VISIBLE_POSITION, currentVisiblePosition);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
