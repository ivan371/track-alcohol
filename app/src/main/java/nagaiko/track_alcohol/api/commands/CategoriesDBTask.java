package nagaiko.track_alcohol.api.commands;

import java.util.List;

import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.api.Response;

import static nagaiko.track_alcohol.api.ApiResponseTypes.CATEGORIES_LIST;

/**
 * Created by altair on 24.12.17.
 */

public class CategoriesDBTask extends DBTask {
    @Override
    public Response<List<String>> execute(DBHelper dbHelper) {
        return new Response<List<String>>(getMessageType(), dbHelper.getCategories());
    }

    @Override
    public ApiTask getRemoteTask() {
        return new CategoriesApiTask();
    }

    @Override
    public int getMessageType() {
        return CATEGORIES_LIST;
    }
}
