package nagaiko.track_alcohol.services;

import okhttp3.HttpUrl;

/**
 * Created by altair on 24.10.17.
 */

public interface IRequest {
    HttpUrl getHttpUrl();
    Class getResponseClass();
}
