package nagaiko.track_alcohol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Request;
import nagaiko.track_alcohol.fragments.ErrorFragment;
import nagaiko.track_alcohol.fragments.RecycleIngridientFragment;
import nagaiko.track_alcohol.fragments.RecyclerFragment;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.ApiDataDownloadService;

public class DetailActivity extends AppCompatActivity implements ServiceConnection, ICallbackOnTask {

    public final String LOG_TAG = this.getClass().getSimpleName();
    private DataStorage dataStorage = DataStorage.getInstance();
    private static final String POSITION = "position";
    private static final String IS_FINISH_BUNDLE_KEY = "is_finish";
    private static final String IS_COCKTAIL_EMPRY = "is_cocktail_empty";
    private static final String ID_COCKTAIL = "idCocktail";
    private boolean isFinish = false;
    private boolean isOnline = false;
    private boolean isEmpty = false;
    private Fragment fragment;
    private int idDrink = 0;
    private DBHelper dataBase;
    int position = 0;
    ApiDataDownloadService.ApiServiceProxy proxy = null;
    TextView instructions;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int defaultValue = 0;
        setContentView(R.layout.activity_detail);
        textView= (TextView) findViewById(R.id.textView);
        instructions = (TextView) findViewById(R.id.textView1);
        dataBase = new DBHelper(this);
        idDrink = getIntent().getIntExtra(ID_COCKTAIL, defaultValue);
        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
            isEmpty = savedInstanceState.getBoolean(IS_COCKTAIL_EMPRY);
        }
        Cocktail cocktail = dataBase.getCocktailById(idDrink);
        // TO_DO есть ли в БД что-нибудь, кроме названия коктеля
        if (cocktail != null) {
            isEmpty = true;
            makeDetail(cocktail);
        }
        else {
//            isEmpty = true; TO_DO если в БД нет такого коктеля, то не биндим сервис
            Intent intent = new Intent(this, ApiDataDownloadService.class);
            bindService(intent, this, Context.BIND_AUTO_CREATE);
        }
    }

    private void makeDetail(Cocktail cocktail) {
        textView.setText(cocktail.getName());
        instructions.setText(cocktail.getInstruction());
        final FragmentManager fm = getSupportFragmentManager();
        if(!cocktail.getIngredients().isEmpty()){
            fragment = new RecyclerFragment();
            fm.beginTransaction().replace(R.id.fragment, fragment, RecycleIngridientFragment.TAG).commit();
        }
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
        if (o == null || o[0] == null || ((Request.ResponseType) o[0]).drinks == null) {
//            Toast.makeText(this, "Что-то пошло не так. Проверьте ваше интернет соединение.", Toast.LENGTH_SHORT).show();
//            dataStorage.setData(DataStorage.COCKTAIL_FILTERED_LIST, null);
        } else {
            dataBase.addOrUpdateCocktail(((Request.ResponseType) o[0]).drinks[0]);
            makeDetail(((Request.ResponseType) o[0]).drinks[0]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isEmpty) {
            unbindService(this);
        }
    }
}
