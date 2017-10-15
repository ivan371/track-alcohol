package nagaiko.track_alcohol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private Download_data_fragment download_data_fragment;
    private static final String DOWNLOAD_TAG = "downloader";
    public static boolean isLoaded = false;
    public static boolean isPaused = false;
    public static StringBuffer downloaded_data = new StringBuffer("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            download_data_fragment = new Download_data_fragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity, download_data_fragment, DOWNLOAD_TAG)
                    .commit();
            download_data_fragment.startTask();
        } else {
            download_data_fragment = (Download_data_fragment) getSupportFragmentManager().findFragmentByTag(DOWNLOAD_TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        download_data_fragment.StartApp(downloaded_data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

}
