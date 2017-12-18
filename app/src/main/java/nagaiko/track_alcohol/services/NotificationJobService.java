package nagaiko.track_alcohol.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by nagai on 18.12.2017.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    private static final String MESSAGE = "Started job with id %s";
    @Override
    public boolean onStartJob(JobParameters params) {
        ScheduledService.startScheduledJob(getApplicationContext(),
                String.format(MESSAGE, params.getJobId()));
        return false;

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Toast.makeText(this,
                "MyJobService.onStopJob()",
                Toast.LENGTH_SHORT).show();
        return false;

    }
}
