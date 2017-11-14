package nagaiko.track_alcohol.api;

/**
 * Created by altair on 13.11.17.
 */

public class Response<T> {

    public int type;
    public T content;

    public Response(int type, T responseObject) {
        this.type = type;
        this.content = responseObject;
    }
}
