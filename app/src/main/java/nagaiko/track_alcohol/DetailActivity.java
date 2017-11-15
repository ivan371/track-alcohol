package nagaiko.track_alcohol;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import nagaiko.track_alcohol.api.ICallbackOnTask;
import nagaiko.track_alcohol.api.Request;
import nagaiko.track_alcohol.fragments.CategoryListFragment;
import nagaiko.track_alcohol.models.Cocktail;
import nagaiko.track_alcohol.recyclerview.IngredientRecyclerAdapter;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_INFO;
import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_THUMB;

public class DetailActivity extends AppCompatActivity implements DataStorage.Subscriber {

    public final String LOG_TAG = this.getClass().getSimpleName();
    private DataStorage dataStorage;
    private static final String POSITION = "position";
    private static final String IS_FINISH_BUNDLE_KEY = "is_finish";
    private static final String IS_COCKTAIL_EMPRY = "is_cocktail_empty";
    private static final String ID_COCKTAIL = "idCocktail";
    private boolean isFinish = false;
    private boolean isOnline = false;
    private boolean isEmpty = false;
    private int idDrink = 0;
    int position = 0;
    private TextView instructions;
    private TextView textView;
    private ImageView thumb;


    private Cocktail cocktail;
    private Bitmap thumbBm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataStorage = DataStorage.getInstanceOrCreate(this);

        int defaultValue = 0;
        setContentView(R.layout.activity_detail);
        textView = (TextView) findViewById(R.id.textView);
        instructions = (TextView) findViewById(R.id.textView1);
        thumb = (ImageView) findViewById(R.id.cocktail_thumb);

        idDrink = getIntent().getIntExtra(ID_COCKTAIL, defaultValue);
        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
            isEmpty = savedInstanceState.getBoolean(IS_COCKTAIL_EMPRY);
        }

        dataStorage.subscribe(this);
        cocktail = dataStorage.getCocktailById(idDrink);
        thumbBm = dataStorage.getCocktailThumb(idDrink);
        // TO_DO есть ли в БД что-нибудь, кроме названия коктеля
        if (cocktail != null) {
            isEmpty = true;
            render();
        }
    }

    private void setCocktail(Cocktail cocktail) {
        this.cocktail = cocktail;
        render();
    }

    private void setThumb(Bitmap thumb) {
        if (thumb != null) {
            thumbBm = thumb;
        }
    }

    private void render() {
        textView.setText(cocktail.getName());
        instructions.setText(cocktail.getInstruction());
        ArrayList<Cocktail.Ingredient> ingredients = cocktail.getIngredients();
        if (!ingredients.isEmpty()) {
            RecyclerView ingredientsView = (RecyclerView) findViewById(R.id.ingredients);
            IngredientRecyclerAdapter adapter = new IngredientRecyclerAdapter(this, ingredients);
            ingredientsView.setAdapter(adapter);
            ingredientsView.setLayoutManager(new LinearLayoutManager(this));
            ingredientsView.setHasFixedSize(true);
            RecyclerView.ItemDecoration itemDecoration = new
                    DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//            ingredientsView.addItemDecoration(itemDecoration);
        }
        if (thumbBm != null) {
            thumb.setImageBitmap(thumbBm);
        }
    }


    @Override
    protected void onResume() {
        isOnline = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnline = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_FINISH_BUNDLE_KEY, isFinish);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
    }

    @Override
    protected void onDestroy() {
        dataStorage.unsubscribe(this);
        super.onDestroy();
    }

    @Override
    public void onDataUpdated(int dataType) {
        Log.d(LOG_TAG, "onDataUpdated:" + dataType);
        if (dataType == COCKTAIL_INFO) {
            setCocktail(dataStorage.getCocktailById(idDrink));
        } else if (dataType == COCKTAIL_THUMB) {
            setThumb(dataStorage.getCocktailThumb(idDrink));
            render();
        }
    }

    @Override
    public void onDataUpdateFail() {
        Toast.makeText(this, "Can't download data", Toast.LENGTH_SHORT).show();
    }

}
