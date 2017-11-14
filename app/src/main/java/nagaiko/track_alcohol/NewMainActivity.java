package nagaiko.track_alcohol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import nagaiko.track_alcohol.models.Cocktail;

//import nagaiko.track_alcohol.api.ICallbackOnTask;
//import nagaiko.track_alcohol.api.Request;
//import nagaiko.track_alcohol.services.ApiDataDownloadService;

/**
 * Created by Konstantin on 23.10.2017.
 */

public class NewMainActivity extends AppCompatActivity implements DataStorage.Subscriber {

    public final String LOG_TAG = this.getClass().getSimpleName();
    private static final String DOWNLOAD_TAG = "downloader";

    private static final String IS_FINISH_BUNDLE_KEY = "is_finish";
    private boolean isFinish = false;
    private boolean isOnline = false;
    private DataStorage dataStorage = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataStorage = DataStorage.getInstanceOrCreate(this);

        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
        }
        dataStorage.subscribe(this);
        if (dataStorage.getCocktailsByCategory("Ordinary Drink").size() != 0) {
            goToNextActivity();
        }
    }

    private void goToNextActivity() {
        if (isOnline) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
            finish();
        }
        isFinish = true;
    }

    @Override
    protected void onResume() {
        isOnline = true;
        if (isFinish) {
            goToNextActivity();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnline = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_FINISH_BUNDLE_KEY, isFinish);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
    }

    @Override
    protected void onStop() {
        dataStorage.unsubscribe(this);
        super.onStop();
    }

    @Override
    public void onDataUpdated(int dataType) {
        Log.d(LOG_TAG, "onDataUpdated");
        goToNextActivity();
    }
}
