package nagaiko.track_alcohol.api.commands;

import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.api.Response;

/**
 * Created by altair on 24.12.17.
 */

abstract public class DBTask extends BaseHandlerTask {

    public DBTask() {
    }

    abstract public Response execute(DBHelper dbHelper);

    abstract public ApiTask getRemoteTask();
}
