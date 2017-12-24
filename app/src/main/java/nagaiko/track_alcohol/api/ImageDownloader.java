package nagaiko.track_alcohol.api;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 * Created by altair on 23.12.17.
 */

public class ImageDownloader extends HandlerThread {
    private ImageDownloaderListener imageDownloaderListener;

    public ImageDownloader(String name) {
        super(name);
    }

    public ImageDownloader(String name, int priority) {
        super(name, priority);
    }

    public interface ImageDownloaderListener {
        void onImageDownloaded(String url, Bitmap image);
    }

    public void setImageDownloadedListener(ImageDownloaderListener listener) {
        imageDownloaderListener = listener;
    }
}
