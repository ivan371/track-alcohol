package nagaiko.track_alcohol.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import nagaiko.track_alcohol.R;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class ErrorFragment extends Fragment {
    public static final String TAG = ErrorFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final LinearLayout l = (LinearLayout) inflater.inflate(R.layout.error_layout, container, false);

        return l;
    }
}
