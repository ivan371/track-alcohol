package nagaiko.track_alcohol;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nagaiko.track_alcohol.api.ApiDataDownloader;
import nagaiko.track_alcohol.api.GetCocktailThumbAsyncTask;
import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.api.commands.ApiTask;
import nagaiko.track_alcohol.api.commands.BaseHandlerTask;
import nagaiko.track_alcohol.api.commands.CategoriesApiTask;
import nagaiko.track_alcohol.api.commands.CategoriesDBTask;
import nagaiko.track_alcohol.api.commands.CocktailByIdApiTask;
import nagaiko.track_alcohol.api.commands.CocktailByIdDBTask;
import nagaiko.track_alcohol.api.commands.CocktailInCategoryApiTask;
import nagaiko.track_alcohol.api.commands.CocktailInCategoryDBTask;
import nagaiko.track_alcohol.api.commands.DBTask;
import nagaiko.track_alcohol.models.Cocktail;

import static nagaiko.track_alcohol.api.ApiResponseTypes.CATEGORIES_LIST;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_INFO;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_THUMB;

/**
 * Created by altair on 24.10.17.
 */

public class DataStorage implements ICallbackOnTask, DBHandlerThread.ICallbackOnTask {
    private static DataStorage _instance = null;

    public synchronized static DataStorage getInstanceOrCreate(Context context) {
        if (_instance == null) {
            _instance = new DataStorage(context);
            _instance.initDownloaders();
        }
        return _instance;
    }

    public static DataStorage getInstance() {
        return _instance;
    }

    public interface Subscriber {
        void onDataUpdated(int dataType);

        void onDataUpdateFail();
    }

    public interface ApiDataSubscriber {
        void onDataLoaded(int type, Response response);

        void onDataLoadFailed();
    }

    public static final int COCKTAIL_FILTERED_LIST = 0;

    private DBHelper dbHelper;
    private DBHandlerThread dbHandlerThread;
    private ApiDataDownloader apiDataDownloader;
    private File cacheDir;
    private int imageSize;
    private LruCache<Integer, Bitmap> _imageCache;
    private String apiKey;
    private ArrayList<Subscriber> subscribers;

    private Map<BaseHandlerTask, List<ApiDataSubscriber>> commandSubscriberMap;

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

        commandSubscriberMap = new HashMap<>();
    }

    private void initDownloaders() {
        apiDataDownloader = new ApiDataDownloader("APIDataDownloader", apiKey, this);
        dbHandlerThread = new DBHandlerThread("DB Helper Thread", dbHelper, this);

    }

    private static int updateImageSize(DisplayMetrics dm) {
        int h = dm.heightPixels;
        int w = dm.widthPixels;
        if (w > h) {
            int tmp = w;
            w = h;
            h = tmp;
        }
        return (int) (Math.min(h * 0.7f, w * 0.7f) + 0.5f);
    }

    private ApiTask getCategoriesAsyncTask() {
        return new CategoriesApiTask();
    }

    private ApiTask getCocktailsInCategoryAsyncTask(String category) {
        return new CocktailInCategoryApiTask(category);
    }

    private ApiTask getCocktailByIdAsyncTask(int cocktailId) {
        return new CocktailByIdApiTask(cocktailId);
    }

    private GetCocktailThumbAsyncTask getCocktailThumbAsyncTask() {
        return new GetCocktailThumbAsyncTask(cacheDir, imageSize, this);
    }

    private void addSubscriber(BaseHandlerTask task, ApiDataSubscriber subscriber) {
        if (!commandSubscriberMap.containsKey(task)) {
            commandSubscriberMap.put(task, new ArrayList<ApiDataSubscriber>());
        }
        commandSubscriberMap.get(task).add(subscriber);
    }

    private void moveSubscribers(BaseHandlerTask from, BaseHandlerTask to) {
        List<ApiDataSubscriber> subscribers = commandSubscriberMap.get(from);
        commandSubscriberMap.put(to, subscribers);
    }

    public void getCategories(ApiDataSubscriber subscriber) {
        final DBTask dbTask = new CategoriesDBTask();
        addSubscriber(dbTask, subscriber);
        dbHandlerThread.addTask(dbTask);
    }

    public void getCocktailsByCategory(ApiDataSubscriber subscriber, String category) {
        DBTask dbTask = new CocktailInCategoryDBTask(category);
        addSubscriber(dbTask, subscriber);
        dbHandlerThread.addTask(dbTask);
    }

    public void getCocktailById(ApiDataSubscriber subscriber, int id) {
        DBTask dbTask = new CocktailByIdDBTask(id);
        addSubscriber(dbTask, subscriber);
        dbHandlerThread.addTask(dbTask);
    }

//    public Bitmap getCocktailThumb(int id) {
//        Bitmap bm = _imageCache.get(id);
//        if (bm == null) {
//            Cocktail cocktail = getCocktailById(id);
//            if (cocktail != null) {
//                GetCocktailThumbAsyncTask task = getCocktailThumbAsyncTask();
//                Bundle bundle = GetCocktailThumbAsyncTask.GetParametersBunble(id, cocktail.getThumb());
//                task.execute(bundle);
//            }
//        }
//        return bm;
//    }

//    public boolean subscribe(Subscriber subscriber) {
//        return subscribers.add(subscriber);
//    }

    public void unsubscribe(Subscriber subscriber) {
        for (Map.Entry<BaseHandlerTask, List<ApiDataSubscriber>> entry : commandSubscriberMap.entrySet()) {
            BaseHandlerTask task = entry.getKey();
            List<ApiDataSubscriber> subsList = entry.getValue();
            subsList.remove(subscriber);
        }
    }

    private void notifySubscribers(BaseHandlerTask task, int type, Response response) {
        for (ApiDataSubscriber subscriber : commandSubscriberMap.get(task)) {
            subscriber.onDataLoaded(type, response);
        }
    }

    @Override
    public void onPostExecute(ApiTask task, int type, Response response) {
        if (response == null) {
            return;
        }
        boolean dataUpdated = false;
        switch (type) {
            case CATEGORIES_LIST:
                List<String> categories = (List<String>) response.content;
                if (categories != null) {
                    for (String category : categories) {
                        dbHelper.addCategory(category);
                    }
                    dataUpdated = true;
                }
                break;

            case COCKTAIL_LIST:
                Cocktail[] cocktails = (Cocktail[]) response.content;
                if (cocktails != null) {
                    for (Cocktail cocktail : cocktails) {
                        dbHelper.addOrUpdateCocktail(cocktail);
                    }
                    dataUpdated = true;
                }
                break;
            case COCKTAIL_INFO:
                Cocktail cocktail = (Cocktail) response.content;
                if (cocktail != null) {
                    dbHelper.addOrUpdateCocktail(cocktail);
                    dataUpdated = true;
                }
                break;
            case COCKTAIL_THUMB:
                Pair<Integer, Bitmap> idWithBm = (Pair<Integer, Bitmap>) response.content;
                if (idWithBm != null) {
                    int cocktailId = idWithBm.first;
                    Bitmap bm = idWithBm.second;
                    _imageCache.put(cocktailId, bm);
                    dataUpdated = true;
                }
                break;
        }
        if (dataUpdated) {
            notifySubscribers(task, type, response);
        }
    }

    @Override
    public void onFailExecute(ApiTask task) {
        for (ApiDataSubscriber subscriber : commandSubscriberMap.get(task)) {
            subscriber.onDataLoadFailed();
        }
    }

    @Override
    public void onLocalDataLoaded(DBTask task, int type, Response response) {
        notifySubscribers(task, type, response);
    }

    @Override
    public void onLocalDataLoadFailed(DBTask task) {
        ApiTask remoteTask = task.getRemoteTask();
        moveSubscribers(task, remoteTask);
        apiDataDownloader.addTask(remoteTask);
    }

}
