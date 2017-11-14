package nagaiko.track_alcohol.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.IRequest;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;

/**
 * Created by altair on 13.11.17.
 */

public class GetCocktailsInCategoryAsyncTask extends BaseApiAsyncTask<String, Cocktail[]> {

    private String category;

    public GetCocktailsInCategoryAsyncTask(String apiKey, ICallbackOnTask callbackOnTask) {
        super(apiKey, callbackOnTask);
    }

    @Override
    protected IRequest getApiRequest(String... params) {
        category = params[0];
        return new Request.Builder(apiKey).setFilterMethod(null, null, params[0], null).build();
    }

    private static class ResponseType {
        Cocktail[] drinks;
    }

    @Override
    protected Response<Cocktail[]> getApiResponse(String responseBody) {
        Gson gson = new GsonBuilder().create();
        ResponseType response =  gson.fromJson(
                responseBody,
                ResponseType.class
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
