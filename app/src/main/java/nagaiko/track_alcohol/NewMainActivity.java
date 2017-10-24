package nagaiko.track_alcohol;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.Loader;
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

    CocktailsInCategoryDowloadFragment categoriesFragment;

    private boolean isFinish = false;
    private DataStorage dataStorage = DataStorage.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ApiDataDownloadService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    /*
        В этом методе два костыля:
        1) По хорошему в следующее activity нужно прокинуть массив из Cocktail. Для этого этот класс надо отналедовать от интерфейса Parseble
        (https://developer.android.com/reference/android/os/Parcelable.html). Я пока с ним не разбирался, постараюсь к 9 вечера завтра
        разобраться. Ну либо придумаем второй вариант. Пока я прокидываю в следующее activity массив с названиями(массив String)
        2) Я походу не совсем разобрался в Loader, но походу метод onLoaderReset вызывается в не зависимости от успешности закачки. Если так,
        то у нас 2 проблему. Первая, этот метод полностью вычищает закачанные данные, то есть их надо сохранять отдельно, что я собственно и
        сделал(хотя по мне это жуткий костыль). Второе, я не понял, как этот Loader заканчивается => как заканчивается первое Activity. Поэтому
        напиши мне как это работает, либо исправь мой код.
     */
    @Override
    public void onPostExecute(Object[] o) {
        Log.d(LOG_TAG, "onLoadFinished");

        dataStorage.setData(DataStorage.COCKTAIL_FILTERED_LIST, ((Request.ResponseType)o[0]).drinks);
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ApiDataDownloadService.ApiServiceProxy proxy = (ApiDataDownloadService.ApiServiceProxy) iBinder;
        String categoryName = "Ordinary_Drink";

        Request request = (Request) proxy.getRequestBulder().setFilterMethod(null, null, categoryName, null)
                .build();
        proxy.sendApiRequest(this, request);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(this);
    }
}
