package nagaiko.track_alcohol.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_THUMB;


/**
 * Created by altair on 23.12.17.
 */

public class ImageDownloader<T> extends HandlerThread {
    private ImageDownloaderListener imageDownloaderListener;
    final private Map<T, String> imageViewUrlMap;
    private File cacheDir;
    private String _name;
    private int _imageSize;
    private Handler downloadHandler;

    public ImageDownloader(String name,
                           File cacheDir, int imageSize,
                           ImageDownloaderListener<T> imageDownloaderListener,
                           Map<T, String> imageViewUrlMap) {
        super(name);
        this.imageDownloaderListener = imageDownloaderListener;
        this.imageViewUrlMap = imageViewUrlMap;
        this.cacheDir = cacheDir;
        this._imageSize = imageSize;
    }

    public interface ImageDownloaderListener<T> {
        void onImageLoaded(T imageView, String url, Bitmap image);
        void onImageLoadFailed(T imageView, String url);
    }

    public void prepareHandler() {
        downloadHandler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                T target = (T)msg.obj;
                String thumbUrl = imageViewUrlMap.get(target);
                Bitmap bm = handleTask(thumbUrl);
                notifySubscribers(target, thumbUrl, bm);
            }
        };
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    protected Bitmap decodeFile(File file) {
        try {
            InputStream is = new FileInputStream(file);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, opt);
            int sc = calculateInSampleSize(opt, _imageSize, _imageSize);
            //is.reset();
            opt.inSampleSize = sc;
            opt.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, opt);
            Log.d("LOAD_IMAGE", " name = " + _name + " w = " + bitmap.getWidth() + " h = " + bitmap.getHeight());
            return bitmap;
        } catch (IOException e) {
            //Log.e("LoadImageTask", "LoadImageTask.LoadBitmap IOException " + e.getMessage(), e);
        }
        return null;
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    private Bitmap handleTask(String thumbUrl) {


        String _name = "images_" + thumbUrl.replace('/', '_');
        try {
            File file = new File(cacheDir, _name);
            Bitmap bitmap = decodeFile(file);
            if (null == bitmap) {
                URL url = new URL(thumbUrl);
                InputStream is = url.openConnection().getInputStream();
                OutputStream os = new FileOutputStream(file);
                CopyStream(is, os);
                os.close();
                bitmap = decodeFile(file);
            }
            return bitmap;

        } catch (IOException e) {
            Log.e("LoadImageTask", "LoadImageTask.LoadBitmap IOException " + e.getMessage(), e);
        }
        return null;
    }

    private void notifySubscribers(T target, String url, Bitmap bm) {
        if (imageDownloaderListener != null) {
            if (bm == null) {
                imageDownloaderListener.onImageLoadFailed(target, url);
            } else {
                imageDownloaderListener.onImageLoaded(target, url, bm);
            }
        }
    }

    public void addTask(T target) {
        downloadHandler.obtainMessage(COCKTAIL_THUMB, target).sendToTarget();
    }

}
