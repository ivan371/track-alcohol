package nagaiko.track_alcohol.api.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import nagaiko.track_alcohol.api.GetCocktailsInCategoryAsyncTask;
import nagaiko.track_alcohol.api.Request;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;
import okhttp3.HttpUrl;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;

/**
 * Created by altair on 24.12.17.
 */

public class CocktailInCategoryApiTask extends ApiTask<List<Cocktail>> {
    private String category;

    public CocktailInCategoryApiTask(String category) {
        this.category = category;
    }

    @Override
    public int getMessageType() {
        return COCKTAIL_LIST;
    }

    @Override
    public HttpUrl getRequestUrl(String apiKey) {
        return new Request.Builder(apiKey)
                .setFilterMethod(null, null, category, null)
                .build().getHttpUrl();
    }

    private static class ResponseType {
        List<Cocktail> drinks;
    }

    @Override
    public Response<List<Cocktail>> makeResponse(String responseString) {
        Gson gson = new GsonBuilder().create();
        CocktailInCategoryApiTask.ResponseType response =  gson.fromJson(
                responseString,
                CocktailInCategoryApiTask.ResponseType.class
        );
        if (response == null) {
            return null;
        }
        for (Cocktail cocktail: response.drinks) {
            cocktail.setCategoryName(category);
        }
        return new Response<>(COCKTAIL_LIST, response.drinks);
    }
}
