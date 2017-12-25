package nagaiko.track_alcohol.api.commands;

import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_INFO;

/**
 * Created by altair on 24.12.17.
 */

public class CocktailByIdDBTask extends DBTask {
    private int cocktailID;

    public CocktailByIdDBTask(int cocktailID) {
        this.cocktailID = cocktailID;
    }

    @Override
    public int getMessageType() {
        return COCKTAIL_INFO;
    }

    @Override
    public Response<Cocktail> execute(DBHelper dbHelper) {
        return new Response<Cocktail>(getMessageType(), dbHelper.getCocktailById(cocktailID));
    }

    @Override
    public ApiTask getRemoteTask() {
        return new CocktailByIdApiTask(cocktailID);
    }
}
