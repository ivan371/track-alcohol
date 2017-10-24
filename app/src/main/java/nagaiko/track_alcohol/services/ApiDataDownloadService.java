package nagaiko.track_alcohol.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import nagaiko.track_alcohol.DataStorage;
import nagaiko.track_alcohol.api.ApiAsyncTask;
import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.IProxyInterface;
import nagaiko.track_alcohol.api.Request;

public class ApiDataDownloadService extends Service {

    public class ApiServiceProxy extends Binder implements IProxyInterface {

        @Override
        public IRequestBuilder getRequestBulder() {
            return new Request.Builder(ApiDataDownloadService.this);
        }

        public void sendApiRequest(ICallbackOnTask callbackOnTask, Request request) {
            ApiAsyncTask asyncTask = new ApiAsyncTask(callbackOnTask);
            asyncTask.execute(request);
        }

        public void sendManyApiRequests(ICallbackOnTask callbackOnTask, Request... requests) {
            ApiAsyncTask asyncTask = new ApiAsyncTask(callbackOnTask);
            asyncTask.execute(requests);
        }
    }

    private static DataStorage dataStorage = DataStorage.getInstance();

    public ApiDataDownloadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ApiServiceProxy();
    }

//    public void sendApiRequest(ICallbackOnTask callbackOnTask, Request request) {
//        ApiAsyncTask asyncTask = new ApiAsyncTask(callbackOnTask);
//        asyncTask.execute(request);
//    }
//
//    public void sendManyApiRequests(ICallbackOnTask callbackOnTask, Request... requests) {
//        ApiAsyncTask asyncTask = new ApiAsyncTask(callbackOnTask);
//        asyncTask.execute(requests);
//    }


}
