package nagaiko.track_alcohol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import nagaiko.track_alcohol.fragments.CategoriesListDownloadFragment;
import nagaiko.track_alcohol.fragments.CocktailsInCategoryDowloadFragment;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 23.10.2017.
 */

public class NewMainActivity extends AppCompatActivity implements CategoriesListDownloadFragment.OnFragmentDataLoadedListener {

    public final String LOG_TAG = this.getClass().getSimpleName();
    private static final String DOWNLOAD_TAG = "downloader";

    CocktailsInCategoryDowloadFragment categoriesFragment;

    private boolean isFinish = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoriesFragment = (CocktailsInCategoryDowloadFragment) getSupportFragmentManager()
                .findFragmentByTag(DOWNLOAD_TAG);
        if (categoriesFragment == null) {
            categoriesFragment = new CocktailsInCategoryDowloadFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity, categoriesFragment, DOWNLOAD_TAG)
                    .commit();
        }

    }

    @Override
    public Loader<Cocktail[]> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        return categoriesFragment.getLoader(this, "Ordinary_Drink");
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
    public void onLoadFinished(Loader<Cocktail[]> loader, Cocktail[] data) {
        Log.d(LOG_TAG, "onLoadFinished");

        String[] names = new String[data.length];
        for (int i = 0; i < data.length; i++){
            names[i] = data[i].name;
        }
        Log.d(LOG_TAG, Integer.toString(names.length));
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("names", names);
        isFinish = true;
        startActivity(intent);
        finish();

    }

    @Override
    public void onLoaderReset(Loader<Cocktail[]> loader) {
        Log.d(LOG_TAG, "onLoaderReset");
        if (!isFinish){
            String[] names = null;
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("names", names);
            startActivity(intent);
            finish();
        }


    }

}
