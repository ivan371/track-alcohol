package nagaiko.track_alcohol.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_THUMB;

/**
 * Created by altair on 14.11.17.
 */

public class GetCocktailThumbAsyncTask extends AsyncTask<Bundle, Void, Response<Pair<Integer, Bitmap>>> {

    public final static String COCKTAIL_ID_BUNDLE_KEY = "COCKTAIL_ID_BUNDLE_KEY";
    public final static String URL_BUNDLE_KEY = "URL_BUNDLE_KEY";

    private File cacheDir;
    private String _name;
    private int _imageSize;

    private ICallbackOnTask callbackOnTask;

    public GetCocktailThumbAsyncTask(File cacheDir, int imageSize, ICallbackOnTask callbackOnTask) {
        this.cacheDir = cacheDir;
        this._imageSize = imageSize;
        this.callbackOnTask = callbackOnTask;
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

    public static Bundle GetParametersBunble(int cocktailId, String thumbUrl) {
        Bundle bundle = new Bundle();
        bundle.putInt(COCKTAIL_ID_BUNDLE_KEY, cocktailId);
        bundle.putString(URL_BUNDLE_KEY, thumbUrl);
        return bundle;
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

    @Override
    protected Response<Pair<Integer, Bitmap>> doInBackground(Bundle... params) {
        int cocktailId = params[0].getInt(COCKTAIL_ID_BUNDLE_KEY);
        String thumbUrl = params[0].getString(URL_BUNDLE_KEY);

        _name = "images_" + cocktailId;
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
            return new Response<>(
                    COCKTAIL_THUMB,
                    new Pair<>(cocktailId, bitmap)
            );

        } catch (IOException e) {
            Log.e("LoadImageTask", "LoadImageTask.LoadBitmap IOException " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Response<Pair<Integer, Bitmap>> response) {
        if (callbackOnTask != null) {
            if (response == null) {
                callbackOnTask.onFailExecute();
            } else {
                callbackOnTask.onPostExecute(response.type, response);
            }
        }
    }
}
