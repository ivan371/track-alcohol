package nagaiko.track_alcohol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Download_data_fragment download_data_fragment;
    private static final String DOWNLOAD_TAG = "downloader";

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
    protected void onPause() {
        super.onPause();
        finish();
    }

}
