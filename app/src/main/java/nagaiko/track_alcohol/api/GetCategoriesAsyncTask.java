package nagaiko.track_alcohol.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.services.IRequest;

import static nagaiko.track_alcohol.api.ApiResponseTypes.CATEGORIES_LIST;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_LIST;

/**
 * Created by altair on 15.11.17.
 */

public class GetCategoriesAsyncTask extends BaseApiAsyncTask<Void, ArrayList<String>> {

    public GetCategoriesAsyncTask(String apiKey, ICallbackOnTask callbackOnTask) {
        super(apiKey, callbackOnTask);
    }

    @Override
    protected IRequest getApiRequest(Void... params) {
        return new Request.Builder(apiKey).setListMethod(Request.LIST_CATEGORIES_METHOD).build();
    }

    private static class ResponseType {
        Cocktail[] drinks;
    }

    @Override
    protected Response<ArrayList<String>> getApiResponse(String responseBody) {
        Gson gson = new GsonBuilder().create();
        GetCategoriesAsyncTask.ResponseType response =  gson.fromJson(
                responseBody,
                GetCategoriesAsyncTask.ResponseType.class
        );
        if (response == null) {
            return null;
        }

        ArrayList<String> categories = new ArrayList<>();
        for (Cocktail cocktail: response.drinks) {
            categories.add(cocktail.getCategoryName());
        }
        return new Response<>(CATEGORIES_LIST, categories);
    }
}
