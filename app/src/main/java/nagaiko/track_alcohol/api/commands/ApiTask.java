package nagaiko.track_alcohol.api.commands;

import nagaiko.track_alcohol.api.Response;
import okhttp3.HttpUrl;

/**
 * Created by altair on 24.12.17.
 */

public abstract class ApiTask<T> extends BaseHandlerTask {
    abstract public HttpUrl getRequestUrl(String apiKey);
    abstract public Response<T> makeResponse(String responseString);
}
