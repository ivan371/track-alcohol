package nagaiko.track_alcohol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import nagaiko.track_alcohol.fragments.ErrorFragment;
import nagaiko.track_alcohol.fragments.RecyclerFragment;
import nagaiko.track_alcohol.models.Cocktail;

public class ListActivity extends AppCompatActivity {

    public final String LOG_TAG = this.getClass().getSimpleName();

    private Fragment fragment;

    private DataStorage dataStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        final FragmentManager fm = getSupportFragmentManager();
        dataStorage = DataStorage.getInstanceOrCreate(this);

//        Log.d(LOG_TAG, (String)dataStorage.getData(0));

        if (false){
            if (savedInstanceState == null) {
                fragment = new ErrorFragment();
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

    }


    @Override
    protected void onStart(){
        super.onStart();

        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume");
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
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
