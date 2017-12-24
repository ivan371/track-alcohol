package nagaiko.track_alcohol;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.api.commands.ApiTask;
import nagaiko.track_alcohol.api.commands.DBTask;

/**
 * Created by altair on 24.12.17.
 */

public class DBHandlerThread extends HandlerThread {

    public interface ICallbackOnTask {
        void onLocalDataLoaded(DBTask task, int type, Response response);
        void onLocalDataLoadFailed(DBTask task);
    }

    private Handler loadHandler;
    private DBHelper dbHelper;
    private ICallbackOnTask callbackOnTask;

    public DBHandlerThread(String name, DBHelper dbHelper, ICallbackOnTask callbackOnTask) {
        super(name);
        this.dbHelper = dbHelper;
        this.callbackOnTask = callbackOnTask;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        loadHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                DBTask task = (DBTask)msg.obj;
                Response response = task.execute(dbHelper);
                notifySubscribers(task, response);
            }
        };
    }

    private void notifySubscribers(DBTask apiTask, Response response) {
        if (callbackOnTask != null) {
            if (response == null) {
                callbackOnTask.onLocalDataLoadFailed(apiTask);
            } else {
                callbackOnTask.onLocalDataLoaded(apiTask, response.type, response);
            }
        }
    }

    public void addTask(DBTask apiTask) {
        loadHandler.obtainMessage(apiTask.getMessageType(), apiTask).sendToTarget();
    }
}
