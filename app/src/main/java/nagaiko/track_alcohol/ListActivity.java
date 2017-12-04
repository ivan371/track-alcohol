package nagaiko.track_alcohol;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nagaiko.track_alcohol.fragments.CategoryListFragment;

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

        if (savedInstanceState == null) {
            fragment = new CategoryListFragment();
            fm.beginTransaction().replace(R.id.fragment, fragment, CategoryListFragment.TAG).commit();
        }else {
            fragment = getSupportFragmentManager().findFragmentByTag(CategoryListFragment.TAG);
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

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
