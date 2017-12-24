package nagaiko.track_alcohol.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeImageTransform;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;

import nagaiko.track_alcohol.DBHelper;
import nagaiko.track_alcohol.DataStorage;
import nagaiko.track_alcohol.DetailActivity;
import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.recyclerview.ClickCocktailListAdapter;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class CocktailListFragment extends Fragment implements
        ClickCocktailListAdapter.OnItemClickListener, DataStorage.Subscriber {
    public static final String TAG = CategoryListFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";
    private static final String ID_COCKTAIL = "idCocktail";

    ArrayList<Cocktail> data;
    private DataStorage dataStorage = DataStorage.getInstance();
    private FragmentTransaction fragmentTransaction;
    private ClickCocktailListAdapter recyclerAdapter;

    private String category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState");
            Log.d(TAG, Integer.toString(currentVisiblePosition));
            currentVisiblePosition = savedInstanceState.getInt(VISIBLE_POSITION);
        }
        dataStorage.subscribe(this);

    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        currentVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        Log.d(TAG, Integer.toString(currentVisiblePosition));
        super.onSaveInstanceState(outState);
        outState.putInt(VISIBLE_POSITION, currentVisiblePosition);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());

        data = dataStorage.getCocktailsByCategory(category);

        recyclerAdapter = new ClickCocktailListAdapter(getActivity().getLayoutInflater(), data, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setHasFixedSize(true);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
    }

    @Override
    public void onStop() {
        dataStorage.unsubscribe(this);
        super.onStop();
    }

    @Override
    public void onItemClick(View view, int position) {
        DBHelper db = new DBHelper(this.getActivity());
//        ImageView image = (ImageView) view.findViewById(R.id.coctail);
//        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.listdetailtransition);
//        image.startAnimation(animation);
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(ID_COCKTAIL, data.get(position).getId());
//        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.listdetailtransition, R.anim.alpha).toBundle();
//        getActivity().startActivity(intent, bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ImageView image = (ImageView) view.findViewById(R.id.coctail);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this.getActivity(), (View)image, "profile");
            startActivity(intent, options.toBundle());
//            this.getActivity().getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            this.getActivity().getWindow().setSharedElementEnterTransition(new ChangeImageTransform());
        } else {
            startActivity(intent);
        }
//        ImageView image = (ImageView) view.findViewById(R.id.image);
//        Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.listdetailtransition);
//        image.startAnimation(animation);
//        this.getActivity().overridePendingTransition(R.anim.listdetailtransition,R.anim.alpha);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.getActivity().supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataUpdated(int dataType) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        data = dataStorage.getCocktailsByCategory(category);
        fragmentTransaction.detach(this).attach(this).commit();
    }

    @Override
    public void onDataUpdateFail() {
        Snackbar.make(this.getView(), R.string.no_internet, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action, snackbarOnClickListener).show();
    }

    CocktailListFragment cocktailListFragment = this;
    View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            cocktailListFragment.getActivity().overridePendingTransition(R.anim.listdetailtransition, R.anim.alpha);
            getFragmentManager().beginTransaction()
                    .detach(cocktailListFragment).attach(cocktailListFragment).commit();
        }
    };

}