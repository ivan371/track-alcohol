package nagaiko.track_alcohol.api;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by altair on 24.10.17.
 */

//public class ApiAsyncTask<RequestParamsType, ResponseType> extends AsyncTask<RequestParamsType, Integer, ResponseType> {
//
//    public final String LOG_TAG = this.getClass().getSimpleName();
//
//    private OkHttpClient httpClient = null;
//    private ICallbackOnTask callbackOnTask;
//
//    public ApiAsyncTask(ICallbackOnTask callbackOnTask) {
//        super();
//        this.callbackOnTask = callbackOnTask;
//        httpClient = new OkHttpClient();
//    }
//
//    @Override
//    protected ArrayList<ResponseType> doInBackground(RequestParamsType[] objects) {
//        ArrayList<ResponseType> result = new ArrayList<T>();
//        for (int i = 0; i < objects.length; ++i) {
//            Request apiRequest = objects[i];
//            HttpUrl httpUrl = apiRequest.getHttpUrl();
//            Class responseClass = apiRequest.getResponseClass();
//
//            okhttp3.Request request = new okhttp3.Request.Builder()
//                    .url(httpUrl)
//                    .build();
//            Log.d(LOG_TAG, "loadInBackground started" + request.toString());
//            try {
//                Response response = httpClient.newCall(request).execute();
//                Log.d(LOG_TAG, "response status:" + response.isSuccessful());
//                if (response.isSuccessful()) {
//                    Gson gson = new GsonBuilder().create();
//                    T responseObject = gson.fromJson(
//                            response.body().string(),
//                            T
//                    );
//                    if (responseObject != null) {
//                        result[i] = responseObject;
//                    } else {
//                        result[i] = null;
//                    }
//                } else {
//                    result[i] = null;
//                }
//            } catch (IOException | NullPointerException e) {
//                Log.e(LOG_TAG, e.toString());
//                result[i] = null;
//            }
//        }
//        return result;
//    }
//
//    @Override
//    protected void onPostExecute(Object[] o) {
//        if (callbackOnTask != null) {
//            callbackOnTask.onPostExecute(o);
//        }
//    }
//}
