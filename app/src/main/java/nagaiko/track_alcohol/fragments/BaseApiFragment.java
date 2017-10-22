package nagaiko.track_alcohol.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import nagaiko.track_alcohol.models.Cocktail;


public class BaseApiFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cocktail[]> {

    private OnFragmentDataLoadedListener mListener;

    public BaseApiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentDataLoadedListener) {
            mListener = (OnFragmentDataLoadedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentDataLoadedListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cocktail[]> onCreateLoader(int id, Bundle args) {
        return mListener.onCreateLoader(id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cocktail[]> loader, Cocktail[] data) {
        mListener.onLoadFinished(loader, data);
    }

    @Override
    public void onLoaderReset(Loader<Cocktail[]> loader) {
        mListener.onLoaderReset(loader);
    }


    public interface OnFragmentDataLoadedListener extends LoaderManager.LoaderCallbacks<Cocktail[]>{
        // TODO: Update argument type and name
    }
}
