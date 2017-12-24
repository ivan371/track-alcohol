package nagaiko.track_alcohol.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import nagaiko.track_alcohol.services.IRequest;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by altair on 13.11.17.
 */

public abstract class BaseApiAsyncTask<RequestParamsType, ResponseType>
        extends AsyncTask<RequestParamsType, Integer, Response<ResponseType> > {

    private final static String LOG_TAG = BaseApiAsyncTask.class.getSimpleName();


    private OkHttpClient httpClient = null;
    private ICallbackOnTask callbackOnTask;
    protected String apiKey;

    public BaseApiAsyncTask(String apiKey, ICallbackOnTask callbackOnTask) {
        super();
        httpClient = new OkHttpClient();
        this.callbackOnTask = callbackOnTask;
        this.apiKey = apiKey;
    }

    @Override
    protected Response<ResponseType> doInBackground(RequestParamsType... params) {
        ArrayList<ResponseType> result = new ArrayList<>();
        IRequest apiRequest = getApiRequest(params);
        HttpUrl httpUrl = apiRequest.getHttpUrl();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(httpUrl)
                .build();

        Response<ResponseType> responseObject;
        Log.d(LOG_TAG, "loadInBackground started" + request.toString());
        try {
            okhttp3.Response response = httpClient.newCall(request).execute();
            Log.d(LOG_TAG, "response status:" + response.isSuccessful());
            if (response.isSuccessful()) {
                responseObject = getApiResponse(response.body().string());
            } else {
                responseObject = null;
            }
        } catch (IOException | NullPointerException e) {
            Log.e(LOG_TAG, e.toString());
            responseObject = null;
        }
        return responseObject;
    }

    @Override
    protected void onPostExecute(Response<ResponseType> response) {
        if (callbackOnTask != null) {
            if (response == null) {
                callbackOnTask.onFailExecute(null);
            } else {
                callbackOnTask.onPostExecute(null, response.type, response);
            }
        }
    }

    protected abstract IRequest getApiRequest(RequestParamsType... params);

    protected abstract Response<ResponseType> getApiResponse(String responseBody);
}
