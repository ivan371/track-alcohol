package nagaiko.track_alcohol.fragments;

import android.content.Context;
import android.os.Bundle;

import nagaiko.track_alcohol.loaders.CocktailLoader;

/**
 * Created by altair on 22.10.17.
 */

public class CocktailsInCategoryDowloadFragment extends BaseApiFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(CategoriesListDownloadFragment.class.hashCode(), null, this);
    }

    public CocktailLoader getLoader(Context context, String categoryName) {
        return new CocktailLoader.Builder(context)
                .setFilterMethod(null, null, categoryName, null)
                .build();
    }
}
