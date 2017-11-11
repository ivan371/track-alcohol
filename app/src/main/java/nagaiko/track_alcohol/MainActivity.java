package nagaiko.track_alcohol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private DownloadDataFragment downloadDataFragment;
    private static final String DOWNLOAD_TAG = "downloader";
    public static boolean isLoaded = false;
    public static boolean isPaused = false;
    public static StringBuffer downloadedData = new StringBuffer("");
    private DBHelper dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            downloadDataFragment = new DownloadDataFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity, downloadDataFragment, DOWNLOAD_TAG)
                    .commit();
            downloadDataFragment.startTask();
        } else {
            downloadDataFragment = (DownloadDataFragment) getSupportFragmentManager().findFragmentByTag(DOWNLOAD_TAG);
        }
        dataBase = new DBHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        downloadDataFragment.StartApp(downloadedData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

}
