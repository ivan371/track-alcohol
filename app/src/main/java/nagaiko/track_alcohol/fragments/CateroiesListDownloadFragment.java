package nagaiko.track_alcohol.fragments;

import android.content.Context;
import android.os.Bundle;

import nagaiko.track_alcohol.loaders.CocktailLoader;

/**
 * Created by altair on 21.10.17.
 */

public class CateroiesListDownloadFragment extends BaseApiFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(CateroiesListDownloadFragment.class.hashCode(), null, this);
    }

    public CocktailLoader getLoader(Context context) {
        return new CocktailLoader.Builder(context)
                .setListMethod(CocktailLoader.LIST_CATEGORIES_METHOD)
                .build();
    }
}
