package nagaiko.track_alcohol.api;

import nagaiko.track_alcohol.api.commands.ApiTask;

/**
 * Created by altair on 24.10.17.
 */

public interface ICallbackOnTask {
    void onPostExecute(ApiTask task, int type, Response response);
    void onFailExecute(ApiTask task);
}
