package nagaiko.track_alcohol;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class DownloadDataFragment extends Fragment {

    private DownLoadTask downloadTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void startTask() {
        downloadTask = new DownLoadTask();
        downloadTask.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ((downloadTask != null) && (downloadTask.getStatus() == AsyncTask.Status.RUNNING)) {
            downloadTask.cancel(true);
        }
    }

    private class DownLoadTask extends AsyncTask<Object, Object, StringBuffer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected StringBuffer doInBackground(Object... params) {
            URL url = null;
            HttpURLConnection connection = null;
            StringBuffer downloadedData = new StringBuffer("");
            try {
                TimeUnit.SECONDS.sleep(1);
                url = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?c=list");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream is = new BufferedInputStream(connection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    downloadedData.append(line);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return downloadedData;
        }

        @Override
        protected void onPostExecute(StringBuffer result) {
            super.onPostExecute(result);
            MainActivity.isLoaded = true;
            MainActivity.downloadedData = result;
            StartApp(result);
        }

    }

    public void StartApp(StringBuffer downloadedData) {
        if (MainActivity.isLoaded && !MainActivity.isPaused) {
            createList(downloadedData);
        }
    }

    public void createList(StringBuffer result) {
        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra("result", (Serializable) result);
        startActivity(intent);
        getActivity().finish();
    }

}
