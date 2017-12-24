package nagaiko.track_alcohol.api.commands;

import java.util.List;

import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;

/**
 * Created by altair on 24.12.17.
 */

public class CocktailInCategoryDBTask extends DBTask {

    private String category;

    public CocktailInCategoryDBTask(String category) {
        this.category = category;
    }

    @Override
    public int getMessageType() {
        return COCKTAIL_LIST;
    }

    @Override
    public Response<List<Cocktail>> execute(DBHelper dbHelper) {
        return new Response<List<Cocktail>>(getMessageType(), dbHelper.getCocktailsByCategory(category));
    }

    @Override
    public ApiTask getRemoteTask() {
        return new CocktailInCategoryApiTask(category);
    }
}
