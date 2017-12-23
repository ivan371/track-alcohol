package nagaiko.track_alcohol.api;

import java.security.InvalidParameterException;

import nagaiko.track_alcohol.services.IRequest;
import nagaiko.track_alcohol.services.IRequestBuilder;
import okhttp3.HttpUrl;

/**
 * Created by altair on 24.10.17.
 */

public class Request implements IRequest {

    private final static String BASE_API_HOST = "www.thecocktaildb.com";
    private final static String BASE_API_PATH = "api/json/v1/";

    public static final String SEARCH_METHOD = "search.php";
    public static final String LOOKUP_METHOD = "lookup.php";
    public static final String RANDOM_METHOD = "random.php";
    public static final String FILTER_METHOD = "filter.php";
    public static final String LIST_METHOD = "list.php";

    public static final int LIST_CATEGORIES_METHOD = 0;
    public static final int LIST_GLASSES_METHOD = 1;
    public static final int LIST_INGREDIENTS_METHOD = 2;
    public static final int LIST_ALCOHOLIC_METHOD = 3;

    private HttpUrl httpUrl = null;
    private Class responseClass = null;

    public HttpUrl getHttpUrl() {
        return httpUrl;
    }

    public Class getResponseClass() {
        return responseClass;
    }

    public static class Builder implements IRequestBuilder {
        private String apiKey = null;
        private HttpUrl.Builder httpUrl = null;

        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }

        public IRequest build() {
            if (httpUrl == null) {
                throw new InvalidParameterException("You must set api method");
            }
            Request request = new Request();
            request.httpUrl = httpUrl.build();

            return request;
        }

        private String getApiKey() {
            return apiKey;
        }

        private HttpUrl.Builder getNewUrlBuilder() {
            String apiKey = getApiKey();
            return new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_API_HOST)
                    .addPathSegments(BASE_API_PATH)
                    .addPathSegment(apiKey);
        }

        public Builder setCocktailID(int cocktailID) {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(LOOKUP_METHOD)
                    .addQueryParameter("i", Integer.toString(cocktailID));
            return this;
        }

        public Builder setSearchMethod(String cocktailName, String ingredientName) {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(SEARCH_METHOD)
                    .addQueryParameter("s", cocktailName)
                    .addQueryParameter("i", ingredientName);
            return this;
        }

        public Builder setRandomMethod() {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(RANDOM_METHOD);
            return this;
        }

        public Builder setFilterMethod(String ingredientName, String alcoholic,
                                                      String category, String glass) {
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(FILTER_METHOD);
            if (ingredientName != null) {
                httpUrl.addQueryParameter("i", ingredientName);
            }
            if (alcoholic != null) {
                httpUrl.addQueryParameter("a", alcoholic);
            }
            if (category != null) {
                httpUrl.addQueryParameter("c", category);
            }
            if (glass != null) {
                httpUrl.addQueryParameter("g", glass);
            }
            return this;
        }

        public Builder setListMethod(int listType) {
            String queryKey = null;
            switch (listType) {
                case LIST_CATEGORIES_METHOD:
                    queryKey = "c";
                    break;
                case LIST_INGREDIENTS_METHOD:
                    queryKey = "i";
                    break;
                case LIST_GLASSES_METHOD:
                    queryKey = "g";
                    break;
                case LIST_ALCOHOLIC_METHOD:
                    queryKey = "a";
                    break;
                default:
                    throw new InvalidParameterException("listType must be one of the list");
            }
            httpUrl = getNewUrlBuilder()
                    .addPathSegment(LIST_METHOD)
                    .addQueryParameter(queryKey, "list");
            return this;
        }

    }
}
