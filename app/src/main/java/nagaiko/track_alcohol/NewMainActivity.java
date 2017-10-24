package nagaiko.track_alcohol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Request;
import nagaiko.track_alcohol.fragments.CocktailsInCategoryDowloadFragment;
import nagaiko.track_alcohol.services.ApiDataDownloadService;

/**
 * Created by Konstantin on 23.10.2017.
 */

public class NewMainActivity extends AppCompatActivity implements ICallbackOnTask, ServiceConnection {

    public final String LOG_TAG = this.getClass().getSimpleName();
    private static final String DOWNLOAD_TAG = "downloader";

    private static final String IS_FINISH_BUNDLE_KEY = "is_finish";
    private boolean isFinish = false;
    private DataStorage dataStorage = DataStorage.getInstance();
    ApiDataDownloadService.ApiServiceProxy proxy = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
        }

        Intent intent = new Intent(this, ApiDataDownloadService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPostExecute(Object[] o) {
        Log.d(LOG_TAG, "onPostExecute");

        dataStorage.setData(DataStorage.COCKTAIL_FILTERED_LIST, ((Request.ResponseType)o[0]).drinks);
        isFinish = true;
//        Intent intent = new Intent(this, ListActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    protected void onResume() {
        if (isFinish) {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
            finish();
        }
        super.onResume();
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
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        proxy = (ApiDataDownloadService.ApiServiceProxy) iBinder;
        proxy.subscribeOnLoad(this);
        String categoryName = "Ordinary_Drink";

        Request request = (Request) proxy.getRequestBulder().setFilterMethod(null, null, categoryName, null)
                .build();
        proxy.sendApiRequest(this, request);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        if (proxy != null) {
            proxy.unsubscribeOnLoad(this);
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(this);
        super.onDestroy();
    }
}
