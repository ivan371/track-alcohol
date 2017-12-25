package nagaiko.track_alcohol;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import nagaiko.track_alcohol.api.Response;

/**
 * Created by Konstantin on 23.10.2017.
 */

public class MainActivity extends AppCompatActivity implements DataStorage.Subscriber {

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
//        dataStorage.subscribe(this);
        dataStorage.getCategories(this);
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
    public void onDataLoaded(int type, Response response) {
        Log.d(LOG_TAG, "onDataUpdated");
        goToNextActivity();
    }

    @Override
    public void onDataLoadFailed() {
        Snackbar.make(this.findViewById(R.id.imageView), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action, snackbarOnClickListener).show();
    }

    View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            recreate();
        }
    };
}
