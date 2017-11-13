package nagaiko.track_alcohol;


import android.content.Context;

import java.util.ArrayList;

import nagaiko.track_alcohol.api.BaseApiAsyncTask;
import nagaiko.track_alcohol.api.GetCocktailsInCategoryAsyncTask;
import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.IRequest;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;

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
        void onDataUpdated();
    }

    public static final int COCKTAIL_FILTERED_LIST = 0;

    private DBHelper dbHelper;
    private String apiKey;
    private ArrayList<Subscriber> subscribers;

    private DataStorage(Context context) {
        dbHelper = new DBHelper(context);
        apiKey = context.getString(R.string.api_key);
        subscribers = new ArrayList<>();
    }

    private GetCocktailsInCategoryAsyncTask getCocktailsInCategoryAsyncTask() {
        return new GetCocktailsInCategoryAsyncTask(apiKey, this);
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
        if (result == null) {
            //TODO: create async task
        }
        return result;
    }

    public boolean subscribe(Subscriber subscriber) {
        return subscribers.add(subscriber);
    }

    public boolean unsubscribe(Subscriber subscriber) {
        return subscribers.remove(subscriber);
    }

    private void notifySubscribers() {
        for (Subscriber subscriber: subscribers) {
            subscriber.onDataUpdated();
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
        }
        if (dataUpdated) {
            notifySubscribers();
        }
    }
}
