package nagaiko.track_alcohol;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import nagaiko.track_alcohol.api.BaseApiAsyncTask;
import nagaiko.track_alcohol.api.GetCategoriesAsyncTask;
import nagaiko.track_alcohol.api.GetCocktailByIdAsyncTask;
import nagaiko.track_alcohol.api.GetCocktailThumbAsyncTask;
import nagaiko.track_alcohol.api.GetCocktailsInCategoryAsyncTask;
import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.IRequest;

import static nagaiko.track_alcohol.api.ApiResponseTypes.CATEGORIES_LIST;
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
    private File cacheDir;
    private int imageSize;
    private LruCache<Integer, Bitmap> _imageCache;
    private String apiKey;
    private ArrayList<Subscriber> subscribers;

    private DataStorage(Context context) {
        dbHelper = new DBHelper(context);
        cacheDir = context.getCacheDir();
        imageSize = updateImageSize(context.getResources().getDisplayMetrics());

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

    private static int updateImageSize(DisplayMetrics dm) {
        int h = dm.heightPixels;
        int w = dm.widthPixels;
        if (w > h) {
            int tmp = w;
            w = h;
            h = tmp;
        }
        return (int)(Math.min(h * 0.7f, w * 0.7f) + 0.5f);
    }

    private GetCategoriesAsyncTask getCategoriesAsyncTask() {
        return new GetCategoriesAsyncTask(apiKey, this);
    }

    private GetCocktailsInCategoryAsyncTask getCocktailsInCategoryAsyncTask() {
        return new GetCocktailsInCategoryAsyncTask(apiKey, this);
    }

    private GetCocktailByIdAsyncTask getCocktailByIdAsyncTask() {
        return new GetCocktailByIdAsyncTask(apiKey, this);
    }

    private GetCocktailThumbAsyncTask getCocktailThumbAsyncTask() {
        return new GetCocktailThumbAsyncTask(cacheDir, imageSize, this);
    }

    public ArrayList<String> getCategories() {
        ArrayList<String> categories = dbHelper.getCategories();
        if (categories == null || categories.size() == 0) {
            getCategoriesAsyncTask().execute();
        }
        return categories;
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
            Cocktail cocktail = getCocktailById(id);
            if (cocktail != null) {
                GetCocktailThumbAsyncTask task = getCocktailThumbAsyncTask();
                Bundle bundle = GetCocktailThumbAsyncTask.GetParametersBunble(id, cocktail.getThumb());
                task.execute(bundle);
            }
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

        if (response == null) {
            return;
        }
        boolean dataUpdated = false;
        switch (type) {
            case CATEGORIES_LIST:
                ArrayList<String> categories = (ArrayList<String>)response.content;
                if (categories != null) {
                    for (String category: categories) {
                        dbHelper.addCategory(category);
                    }
                    dataUpdated = true;
                }
                break;

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
                break;
        }
        if (dataUpdated) {
            notifySubscribers(type);
        }
    }
}
