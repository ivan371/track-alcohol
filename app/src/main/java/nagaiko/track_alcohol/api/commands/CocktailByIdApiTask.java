package nagaiko.track_alcohol.api.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nagaiko.track_alcohol.api.GetCocktailByIdAsyncTask;
import nagaiko.track_alcohol.api.Request;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;
import okhttp3.HttpUrl;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_INFO;

/**
 * Created by altair on 24.12.17.
 */

public class CocktailByIdApiTask extends ApiTask<Cocktail> {

    private int cocktailId;

    public CocktailByIdApiTask(int cocktailID) {
        this.cocktailId = cocktailID;
    }

    @Override
    public int getMessageType() {
        return COCKTAIL_INFO;
    }

    @Override
    public HttpUrl getRequestUrl(String apiKey) {
        return new Request.Builder(apiKey).setCocktailID(cocktailId).build().getHttpUrl();
    }

    private static class ResponseType {
        Cocktail[] drinks;
    }

    @Override
    public Response<Cocktail> makeResponse(String responseString) {
        Gson gson = new GsonBuilder().create();
        CocktailByIdApiTask.ResponseType response =  gson.fromJson(
                responseString,
                CocktailByIdApiTask.ResponseType.class
        );
        if (response == null) {
            return null;
        }
        return new Response<>(COCKTAIL_INFO, response.drinks[0]);
    }
}
