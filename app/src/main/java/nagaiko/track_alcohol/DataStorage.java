package nagaiko.track_alcohol;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;

import java.util.ArrayList;

import nagaiko.track_alcohol.api.BaseApiAsyncTask;
import nagaiko.track_alcohol.api.GetCocktailByIdAsyncTask;
import nagaiko.track_alcohol.api.GetCocktailsInCategoryAsyncTask;
import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.IRequest;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_INFO;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_THUMB;

/**
 * Created by altair on 24.10.17.
 */

public class DataStorage implements ICallbackOnTask {
    private static DataStorage _instance = null;

    public synchronized static DataStorage getInstanceOrCreate(Context context) {
        if (_instance == null) {
            _instance = new DataStorage(context);
        }
        return _instance;
    }

    public static DataStorage getInstance() {
        return _instance;
    }

    public interface Subscriber {
        void onDataUpdated(int dataType);
    }

    public static final int COCKTAIL_FILTERED_LIST = 0;

    private DBHelper dbHelper;
    private LruCache<Integer, Bitmap> _imageCache;
    private String apiKey;
    private ArrayList<Subscriber> subscribers;

    private DataStorage(Context context) {
        dbHelper = new DBHelper(context);

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        _imageCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };

        apiKey = context.getString(R.string.api_key);
        subscribers = new ArrayList<>();
    }

    private GetCocktailsInCategoryAsyncTask getCocktailsInCategoryAsyncTask() {
        return new GetCocktailsInCategoryAsyncTask(apiKey, this);
    }

    private GetCocktailByIdAsyncTask getCocktailByIdAsyncTask() {
        return new GetCocktailByIdAsyncTask(apiKey, this);
    }

    public ArrayList<Cocktail> getCocktailsByCategory(String category) {
        ArrayList results = dbHelper.getCocktailsByCategory(category);
        if (results == null || results.size() == 0) {
            getCocktailsInCategoryAsyncTask().execute(category);
        }
        return results;
    }

    public Cocktail getCocktailById(int id) {
        Cocktail result = dbHelper.getCocktailById(id);
        if (result == null || !result.isFull()) {
            getCocktailByIdAsyncTask().execute(id);
        }
        return result;
    }

    public Bitmap getCocktailThumb(int id) {
        Bitmap bm = _imageCache.get(id);
        if (bm == null) {
            //TODO: добавить создание таска на загрузку картинки
        }
        return bm;
    }

    public boolean subscribe(Subscriber subscriber) {
        return subscribers.add(subscriber);
    }

    public boolean unsubscribe(Subscriber subscriber) {
        return subscribers.remove(subscriber);
    }

    private void notifySubscribers(int type) {
        for (Subscriber subscriber: subscribers) {
            subscriber.onDataUpdated(type);
        }
    }

    @Override
    public void onPostExecute(int type, Response response) {
        boolean dataUpdated = false;
        switch (type) {
            case COCKTAIL_LIST:
                Cocktail[] cocktails = (Cocktail[])response.content;
                if (cocktails != null) {
                    for (Cocktail cocktail: cocktails) {
                        dbHelper.addOrUpdateCocktail(cocktail);
                    }
                    dataUpdated = true;
                }
                break;
            case COCKTAIL_INFO:
                Cocktail cocktail = (Cocktail)response.content;
                if (cocktail != null) {
                    dbHelper.addOrUpdateCocktail(cocktail);
                    dataUpdated = true;
                }
                break;
            case COCKTAIL_THUMB:
                Pair<Integer, Bitmap> idWithBm = (Pair<Integer, Bitmap>)response.content;
                if (idWithBm != null) {
                    int cocktailId = idWithBm.first;
                    Bitmap bm = idWithBm.second;
                    _imageCache.put(cocktailId, bm);
                    dataUpdated = true;
                }
        }
        if (dataUpdated) {
            notifySubscribers(type);
        }
    }
}
