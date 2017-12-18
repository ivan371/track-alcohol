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
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

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
    JobScheduler jobScheduler;
    private static final int MYJOBID = 1;
    Chronometer chronometer;
    private static final int NOTIFY_ID = 101;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataStorage = DataStorage.getInstanceOrCreate(this);

        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
        }
        dataStorage.subscribe(this);
        if (dataStorage.getCategories().size() != 0) {
            goToNextActivity();
        }
        final ScheduleJobClickListener scheduleJobClickListener = new ScheduleJobClickListener(this);
        new CountDownTimer(300000000, 10000) {
            public void onTick(long millisUntilFinished) {
                scheduleJobClickListener.execute();
            }
            public void onFinish() {
            }
        }.start();
    }

    static class ScheduleJobClickListener {
        private final Context context;
        ScheduleJobClickListener(Context context) {
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void execute() {
            ComponentName serviceComponent = new ComponentName(context, NotificationJobService.class);
            int id = (int) System.currentTimeMillis();
            JobInfo.Builder infoBuilder = new JobInfo.Builder(id, serviceComponent);
            infoBuilder.setMinimumLatency(1000)
                    .setOverrideDeadline(2 * 1000);
            JobInfo info = infoBuilder.build();
            JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(info);
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

    @Override
    public void onDataUpdateFail() {
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
