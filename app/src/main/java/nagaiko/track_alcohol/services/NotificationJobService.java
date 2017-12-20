package nagaiko.track_alcohol.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import nagaiko.track_alcohol.MainActivity;
import nagaiko.track_alcohol.R;

/**
 * Created by nagai on 18.12.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    private static final String MESSAGE = "Started job with id %s";
    private static final int NOTIFY_ID = 101;
    @Override
    public boolean onStartJob(JobParameters params) {
        ScheduledService.startScheduledJob(getApplicationContext(),
                String.format(MESSAGE, params.getJobId()));
        setNotify();
        return false;

    }

    public void setNotify() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = this.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.coctail)
                .setContentTitle("Го бухать!")
                .setContentText("Ты давно не бухал")
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.coctail))
                .setTicker("Твои друзья на НК, а ты нет")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Toast.makeText(this,
                "MyJobService.onStopJob()",
                Toast.LENGTH_SHORT).show();
        return false;

    }
}
