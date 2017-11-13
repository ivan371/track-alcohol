package nagaiko.track_alcohol.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.IRequest;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_INFO;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;

/**
 * Created by altair on 14.11.17.
 */

public class GetCocktailByIdAsyncTask extends BaseApiAsyncTask<Integer, Cocktail> {

    public GetCocktailByIdAsyncTask(String apiKey, ICallbackOnTask callbackOnTask) {
        super(apiKey, callbackOnTask);
    }

    @Override
    protected IRequest getApiRequest(Integer... params) {
        return new Request.Builder(apiKey).setCocktailID(params[0]).build();
    }

    private static class ResponseType {
        Cocktail[] drinks;
    }

    @Override
    protected Response<Cocktail> getApiResponse(String responseBody) {
        Gson gson = new GsonBuilder().create();
        GetCocktailByIdAsyncTask.ResponseType response =  gson.fromJson(
                responseBody,
                GetCocktailByIdAsyncTask.ResponseType.class
        );
        if (response == null) {
            return null;
        }
        return new Response<>(COCKTAIL_INFO, response.drinks[0]);
    }
}
