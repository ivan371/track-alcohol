package nagaiko.track_alcohol.api.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import nagaiko.track_alcohol.api.GetCategoriesAsyncTask;
import nagaiko.track_alcohol.api.Request;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;
import okhttp3.HttpUrl;

import static nagaiko.track_alcohol.api.ApiResponseTypes.CATEGORIES_LIST;

/**
 * Created by altair on 24.12.17.
 */

public class CategoriesApiTask extends ApiTask<List<String>> {
    @Override
    public int getMessageType() {
        return CATEGORIES_LIST;
    }

    @Override
    public HttpUrl getRequestUrl(String apiKey) {
        return new Request.Builder(apiKey)
                .setListMethod(Request.LIST_CATEGORIES_METHOD)
                .build()
                .getHttpUrl();
    }

    private static class ResponseType {
        Cocktail[] drinks;
    }

    @Override
    public Response<List<String>> makeResponse(String responseString) {
        Gson gson = new GsonBuilder().create();
        CategoriesApiTask.ResponseType response =  gson.fromJson(
                responseString,
                CategoriesApiTask.ResponseType.class
        );
        if (response == null) {
            return null;
        }

        ArrayList<String> categories = new ArrayList<>();
        for (Cocktail cocktail: response.drinks) {
            categories.add(cocktail.getCategoryName());
        }
        return new Response<List<String>>(CATEGORIES_LIST, categories);
    }
}
