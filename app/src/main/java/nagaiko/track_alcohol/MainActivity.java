package nagaiko.track_alcohol;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import nagaiko.track_alcohol.api.Response;

import java.util.List;

import nagaiko.track_alcohol.services.NotificationJobService;

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
    private static final long REFRESH_INTERVAL  = 20 * 1000;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataStorage = DataStorage.getInstanceOrCreate(this, new Handler(getMainLooper()));

        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
        }
//        dataStorage.subscribe(this);
        dataStorage.getCategories(this);

        ComponentName componentName = new ComponentName(getApplicationContext(), NotificationJobService.class);
        JobInfo jobInfo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            jobInfo = new JobInfo.Builder(1, componentName)
                    .setMinimumLatency(REFRESH_INTERVAL)
                    .build();
        } else {
            jobInfo = new JobInfo.Builder(1, componentName)
                    .setPeriodic(REFRESH_INTERVAL)
                    .build();
        }
        JobScheduler scheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobInfo);
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
