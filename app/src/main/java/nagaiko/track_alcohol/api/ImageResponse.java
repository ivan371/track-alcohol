package nagaiko.track_alcohol.api;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by altair on 25.12.17.
 */

public class ImageResponse {
    public ImageView imageView;
    public String url;
    public Bitmap bm;

    public ImageResponse(ImageView imageView, String url, Bitmap bm) {
        this.imageView = imageView;
        this.url = url;
        this.bm = bm;
    }
}
