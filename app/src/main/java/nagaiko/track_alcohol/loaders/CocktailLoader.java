package nagaiko.track_alcohol.loaders;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.os.OperationCanceledException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by altair on 21.10.17.
 */

/*
 * Download data from CocktailDB.
 *
 * How to use:
 *  Create Builder instance. Set method and run .build().
 */
public class CocktailLoader extends AsyncTaskLoader<Cocktail[]> {

    public final String LOG_TAG = this.getClass().getSimpleName();

    private final static String BASE_API_HOST = "www.thecocktaildb.com";
    private final static String BASE_API_PATH = "api/json/v1/";
    private static String apiKey = null;

    public static String SEARCH_METHOD = "search.php";
    public static String LOOKUP_METHOD = "lookup.php";
    public static String RANDOM_METHOD = "random.php";
    public static String FILTER_METHOD = "filter.php";
    public static String LIST_METHOD = "list.php";

    public static final int LIST_CATEGORIES_METHOD = 0;
    public static final int LIST_GLASSES_METHOD = 1;
    public static final int LIST_INGREDIENTS_METHOD = 2;
    public static final int LIST_ALCOHOLIC_METHOD = 3;


    private HttpUrl httpUrl = null;
    private OkHttpClient httpClient = new OkHttpClient();

    public static class Builder {
        private Context context = null;
        private HttpUrl.Builder httpUrl = null;

        private CocktailLoader loader;

        public Builder(Context context) {
            this.context = context;
        }

        public CocktailLoader build() {
            if (httpUrl == null) {
                throw new InvalidParameterException("You must set api method");
            }
            CocktailLoader cocktailLoader = new CocktailLoader(context);
            cocktailLoader.httpUrl = httpUrl.build();
            return cocktailLoader;
        }

        private Context getContext() {
            return context;
        }

        private String getApiKey() {
            if (apiKey == null) {
                apiKey = getContext().getString(R.string.api_key);
            }
            return apiKey;
        }

        private HttpUrl.Builder getNewUrlBuilder() {
            String apiKey = getApiKey();
            return new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_API_HOST)
                    .addPathSegments(BASE_API_PATH)
                    .addPathSegment(apiKey);
        }

        public Builder setCocktailID(int cocktailID) {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(LOOKUP_METHOD)
                    .addQueryParameter("i", Integer.toString(cocktailID));
            return this;
        }

        public Builder setSearchMethod(String cocktailName, String ingredientName) {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(SEARCH_METHOD)
                    .addQueryParameter("s", cocktailName)
                    .addQueryParameter("i", ingredientName);
            return this;
        }

        public Builder setRandomMethod() {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(RANDOM_METHOD);
            return this;
        }

        public Builder setFilterMethod(String ingredientName, String alcoholic,
                                       String category, String glass) {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(FILTER_METHOD);
            if (ingredientName != null) {
                httpUrl.addQueryParameter("i", ingredientName);
            }
            if (alcoholic != null) {
                httpUrl.addQueryParameter("a", alcoholic);
            }
            if (category != null) {
                httpUrl.addQueryParameter("c", category);
            }
            if (glass != null) {
                httpUrl.addQueryParameter("g", glass);
            }
            return this;
        }

        public Builder setListMethod(int listType) {
            String queryKey = null;
            switch (listType) {
                case LIST_CATEGORIES_METHOD:
                    queryKey = "c";
                    break;
                case LIST_INGREDIENTS_METHOD:
                    queryKey = "i";
                    break;
                case LIST_GLASSES_METHOD:
                    queryKey = "g";
                    break;
                case LIST_ALCOHOLIC_METHOD:
                    queryKey = "a";
                    break;
                default:
                    throw new InvalidParameterException("listType must be one of the list");
            }
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(LIST_METHOD)
                    .addQueryParameter(queryKey, "list");
            return this;
        }

    }

    public CocktailLoader(Context context) {
        super(context);
        Log.d(LOG_TAG, "CocktailLoader created");
    }

    private static class ResponseObject {
        Cocktail[] drinks;
    }

    @Override
    public
    @Nullable
    Cocktail[] loadInBackground() {
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        Log.d(LOG_TAG, "loadInBackground started" + request.toString());
        try {
            Response response = httpClient.newCall(request).execute();
            Log.d(LOG_TAG, "response status:" + response.isSuccessful());
            if (response.isSuccessful()) {
                Gson gson = new GsonBuilder().create();
                ResponseObject responseObject = gson.fromJson(
                        response.body().string(),
                        ResponseObject.class
                );
                if (responseObject != null && responseObject.drinks != null) {
                    return responseObject.drinks;
                }
            }
        } catch (IOException | NullPointerException e) {
            OperationCanceledException e2 = new OperationCanceledException();
            e2.addSuppressed(e);
            throw e2;
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
//        if (takeContentChanged()) {
        forceLoad();
//        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

}
