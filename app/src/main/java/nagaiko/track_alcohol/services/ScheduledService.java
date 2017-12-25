package nagaiko.track_alcohol.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import nagaiko.track_alcohol.MainActivity;
import nagaiko.track_alcohol.R;

/**
 * Created by nagai on 18.12.2017.
 */

public class ScheduledService extends IntentService {
    private static final String EXTRA_MESSAGE = "extra_message";
    private static final String ACTION = "scheduled_action";
    private static final int NOTIFY_ID = 101;

    public ScheduledService() {
        super("ScheduledService");
    }

    public static void startScheduledJob(Context context, String message) {
        Intent intent = new Intent(context, ScheduledService.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            final String message = intent.getStringExtra(EXTRA_MESSAGE);
            Toast.makeText(getApplicationContext(), "Scheduled service", Toast.LENGTH_LONG).show();
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
