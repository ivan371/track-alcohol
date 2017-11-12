package nagaiko.track_alcohol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Request;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.ApiDataDownloadService;

public class DetailActivity extends AppCompatActivity implements ServiceConnection, ICallbackOnTask {

    public final String LOG_TAG = this.getClass().getSimpleName();
    private DataStorage dataStorage = DataStorage.getInstance();
    private static final String POSITION = "position";
    private static final String IS_FINISH_BUNDLE_KEY = "is_finish";
    private boolean isFinish = false;
    private boolean isOnline = false;
    private int idDrink = 0;
    int position = 0;
    ApiDataDownloadService.ApiServiceProxy proxy = null;
    TextView instructions;
    Cocktail[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int defaultValue = 0;
        TextView textView;
        setContentView(R.layout.activity_detail);
        textView= (TextView) findViewById(R.id.textView);
        data = (Cocktail[])dataStorage.getData(DataStorage.COCKTAIL_FILTERED_LIST);
        position = getIntent().getIntExtra(POSITION, defaultValue);
        idDrink = data[position].getId();
        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
        }
        Intent intent = new Intent(this, ApiDataDownloadService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
        textView.setText(data[position].getName());
    }

    @Override
    protected void onResume() {
        isOnline = true;
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
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        proxy = (ApiDataDownloadService.ApiServiceProxy) iBinder;
        proxy.subscribeOnLoad(this);
        Request request = (Request) proxy.getRequestBulder().setCocktailID(idDrink)
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
    public void onPostExecute(Object[] o) {
        if (o == null || o[0] == null) {
//            Toast.makeText(this, "Что-то пошло не так. Проверьте ваше интернет соединение.", Toast.LENGTH_SHORT).show();
//            dataStorage.setData(DataStorage.COCKTAIL_FILTERED_LIST, null);
        } else {
            Log.d(LOG_TAG, "o");
//            if(((Request.ResponseType) o[0]).drinks == null)
//                Log.d(LOG_TAG, "o");
//            dataStorage.setEl(position, ((Request.ResponseType) o[0]).drinks[0]);
//            ((Request.ResponseType) o[0]).drinks[0].setInstruction();
//            instructions.setText(.instruction);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(this);
    }
}
