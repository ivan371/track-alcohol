package nagaiko.track_alcohol.api;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import nagaiko.track_alcohol.api.commands.ApiTask;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by altair on 24.12.17.
 */

public class ApiDataDownloader extends HandlerThread {

    private final static String LOG_TAG = ApiDataDownloader.class.getSimpleName();

    private String apiKey;
    private OkHttpClient httpClient;
    private Handler downloadHandler;
    private ICallbackOnTask callbackOnTask;

    public ApiDataDownloader(String name, String apiKey, ICallbackOnTask callbackOnTask) {
        super(name);
        this.apiKey = apiKey;
        httpClient = new OkHttpClient();
        this.callbackOnTask = callbackOnTask;
    }

    public ApiDataDownloader(String name, int priority, String apiKey, ICallbackOnTask callbackOnTask) {
        super(name, priority);
        this.apiKey = apiKey;
        httpClient = new OkHttpClient();
        this.callbackOnTask = callbackOnTask;
    }

    public void prepareHandler() {
        downloadHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                ApiTask task = (ApiTask)msg.obj;
                Response response = handleTask(task);
                notifySubscribers(task, response);
            }
        };
    }

    private Response handleTask(ApiTask task) {
        HttpUrl httpUrl = task.getRequestUrl(apiKey);

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(httpUrl)
                .build();

        Response responseObject;
        Log.d(LOG_TAG, "loadInBackground started" + request.toString());
        try {
            okhttp3.Response response = httpClient.newCall(request).execute();
            Log.d(LOG_TAG, "response status:" + response.isSuccessful());
            if (response.isSuccessful()) {
                responseObject = task.makeResponse(response.body().string());
            } else {
                responseObject = null;
            }
        } catch (IOException | NullPointerException e) {
            Log.e(LOG_TAG, e.toString());
            responseObject = null;
        }
        return responseObject;
    }

    private void notifySubscribers(ApiTask apiTask, Response response) {
        if (callbackOnTask != null) {
            if (response == null) {
                callbackOnTask.onFailExecute(apiTask);
            } else {
                callbackOnTask.onPostExecute(apiTask, response.type, response);
            }
        }
    }

    public void addTask(ApiTask apiTask) {
        downloadHandler.obtainMessage(apiTask.getMessageType(), apiTask).sendToTarget();
    }
}
