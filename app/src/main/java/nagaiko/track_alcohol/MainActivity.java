package nagaiko.track_alcohol;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownLoadTask downLoadTask = new DownLoadTask();
        downLoadTask.execute();
    }

    class DownLoadTask extends AsyncTask<Object, Object, StringBuffer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected StringBuffer doInBackground(Object... params) {
            URL url = null;
            HttpURLConnection connection = null;
            StringBuffer chaine = new StringBuffer("");
            try {
                TimeUnit.SECONDS.sleep(1);
                url = new URL("http://www.thecocktaildb.com/api/json/v1/1/list.php?c=list");
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                InputStream is = new BufferedInputStream(connection.getInputStream());
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    chaine.append(line);
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
            return chaine;
        }

        @Override
        protected void onPostExecute(StringBuffer result) {
            super.onPostExecute(result);
            createList(result);
        }
    }

    public void createList(StringBuffer result) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("result", (Serializable) result);
        startActivity(intent);

    }
}
