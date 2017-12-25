package nagaiko.track_alcohol;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nagaiko.track_alcohol.api.ApiDataDownloader;
import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.ImageDownloader;
import nagaiko.track_alcohol.api.ImageResponse;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.api.commands.ApiTask;
import nagaiko.track_alcohol.api.commands.BaseHandlerTask;
import nagaiko.track_alcohol.api.commands.CategoriesDBTask;
import nagaiko.track_alcohol.api.commands.CocktailByIdDBTask;
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

public class DataStorage implements ICallbackOnTask, DBHandlerThread.ICallbackOnTask, ImageDownloader.ImageDownloaderListener<ImageView> {
    private static DataStorage _instance = null;

    public synchronized static DataStorage getInstanceOrCreate(Context context, Handler uiHandler) {
        if (_instance == null) {
            _instance = new DataStorage(context, uiHandler);
            _instance.initDownloaders();
        }
        return _instance;
    }

    public static DataStorage getInstance() {
        return _instance;
    }

    public interface Subscriber {
        void onDataLoaded(int type, Response response);

        void onDataLoadFailed();
    }

    public static final int COCKTAIL_FILTERED_LIST = 0;

    private DBHelper dbHelper;
    private Handler uiHandler;
    private DBHandlerThread dbHandlerThread;
    private ApiDataDownloader apiDataDownloader;
    private ImageDownloader<ImageView> imageDownloader;

    private File cacheDir;
    private int imageSize;
    private LruCache<String, Bitmap> _imageCache;
    private String apiKey;

    private Map<BaseHandlerTask, List<Subscriber>> commandSubscriberMap;

//    private Map<Integer, List<ImageDownloader.ImageDownloaderListener>> imageIdImageViewListener;
    private Map<ImageView, String> imageViewUrlMap;
    private Map<ImageView, Subscriber> imageViewSubscriberMap;

    private DataStorage(Context context, Handler uiHandler) {
        dbHelper = new DBHelper(context);
        cacheDir = context.getCacheDir();
        imageSize = updateImageSize(context.getResources().getDisplayMetrics());
        this.uiHandler = uiHandler;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        _imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };

        apiKey = context.getString(R.string.api_key);
//        subscribers = new ArrayList<>();

        commandSubscriberMap = new HashMap<>();
        imageViewUrlMap = new HashMap<>();
        imageViewSubscriberMap = new HashMap<>();
    }

    private void initDownloaders() {
        apiDataDownloader = new ApiDataDownloader("APIDataDownloader", apiKey, this);
        apiDataDownloader.start();
        apiDataDownloader.prepareHandler();

        dbHandlerThread = new DBHandlerThread("DB Helper Thread", dbHelper, this);
        dbHandlerThread.start();
        dbHandlerThread.prepareHandler();

        imageDownloader = new ImageDownloader<ImageView>("Image downloader", cacheDir, imageSize, this, imageViewUrlMap);
        imageDownloader.start();
        imageDownloader.prepareHandler();

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

    private void addSubscriber(BaseHandlerTask task, Subscriber subscriber) {
        if (!commandSubscriberMap.containsKey(task)) {
            commandSubscriberMap.put(task, new ArrayList<Subscriber>());
        }
        List<Subscriber> subscribers = commandSubscriberMap.get(task);
        if(!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    private void addImageSubscriber(ImageView imageView, Subscriber subscriber, String url) {
        imageViewSubscriberMap.put(imageView, subscriber);
        imageViewUrlMap.put(imageView, url);
    }

    private void moveSubscribers(BaseHandlerTask from, BaseHandlerTask to) {
        List<Subscriber> subscribers = commandSubscriberMap.get(from);
        commandSubscriberMap.put(to, subscribers);
    }

    public void getCategories(Subscriber subscriber) {
        final DBTask dbTask = new CategoriesDBTask();
        addSubscriber(dbTask, subscriber);
        dbHandlerThread.addTask(dbTask);
    }

    public void getCocktailsByCategory(Subscriber subscriber, String category) {
        DBTask dbTask = new CocktailInCategoryDBTask(category);
        addSubscriber(dbTask, subscriber);
        dbHandlerThread.addTask(dbTask);
    }

    public void getCocktailById(Subscriber subscriber, int id) {
        DBTask dbTask = new CocktailByIdDBTask(id);
        addSubscriber(dbTask, subscriber);
        dbHandlerThread.addTask(dbTask);
    }

    public Bitmap getCocktailThumb(Subscriber subscriber, String url, ImageView target) {
        Bitmap bm = _imageCache.get(url);
        if (bm == null) {
            addImageSubscriber(target, subscriber, url);
            imageDownloader.addTask(target);
        }
        return bm;
    }

//    public boolean subscribe(Subscriber subscriber) {
//        return subscribers.add(subscriber);
//    }

    public void unsubscribe(Subscriber subscriber) {
        for (Map.Entry<BaseHandlerTask, List<Subscriber>> entry : commandSubscriberMap.entrySet()) {
            BaseHandlerTask task = entry.getKey();
            List<Subscriber> subsList = entry.getValue();
            subsList.remove(subscriber);
        }
    }

    public void unsubscribeImage(ImageView imageView) {
        imageViewSubscriberMap.remove(imageView);
        imageViewUrlMap.remove(imageView);
    }

    public void unsubscribeImage(Subscriber subscriber) {
        for (Map.Entry<ImageView, Subscriber> entry: imageViewSubscriberMap.entrySet()) {
            if (subscriber.equals(entry.getValue())) {
                imageViewUrlMap.remove(entry.getKey());
                imageViewSubscriberMap.remove(entry.getKey());
            }
        }
    }

    private void notifySubscribers(BaseHandlerTask task, int type, Response response) {
        for (Subscriber subscriber : commandSubscriberMap.get(task)) {
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
                List<Cocktail> cocktails = (List<Cocktail>) response.content;
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
//            case COCKTAIL_THUMB:
//                Pair<Integer, Bitmap> idWithBm = (Pair<Integer, Bitmap>) response.content;
//                if (idWithBm != null) {
//                    int cocktailId = idWithBm.first;
//                    Bitmap bm = idWithBm.second;
//                    _imageCache.put(cocktailId, bm);
//                    dataUpdated = true;
//                }
//                break;
        }
        if (dataUpdated) {
            notifySubscribers(task, type, response);
        }
    }

    @Override
    public void onFailExecute(ApiTask task) {
        for (Subscriber subscriber : commandSubscriberMap.get(task)) {
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

    @Override
    public void onImageLoaded(final ImageView target, String url, Bitmap bitmap) {
        _imageCache.put(url, bitmap);
        final Response<ImageResponse> response = new Response<>(COCKTAIL_THUMB, new ImageResponse(
                target, url, bitmap
        ));

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Subscriber subscriber = imageViewSubscriberMap.get(target);
                subscriber.onDataLoaded(response.type, response);
            }
        });
    }

    @Override
    public void onImageLoadFailed(ImageView imageView, String url) {

    }

}
