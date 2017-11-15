package nagaiko.track_alcohol.api;

/**
 * Created by altair on 24.10.17.
 */

public interface ICallbackOnTask {
    void onPostExecute(int type, Response response);
    void onFailExecute();
}
