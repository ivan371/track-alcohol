package nagaiko.track_alcohol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import nagaiko.track_alcohol.models.Cocktail;
//import nagaiko.track_alcohol.services.ApiDataDownloadService;

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
    TextView instructions;
    TextView textView;

    private Cocktail cocktail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataStorage = DataStorage.getInstanceOrCreate(this);

        int defaultValue = 0;
        setContentView(R.layout.activity_detail);
        textView= (TextView) findViewById(R.id.textView);
        instructions = (TextView) findViewById(R.id.textView1);

        idDrink = getIntent().getIntExtra(ID_COCKTAIL, defaultValue);
        if (savedInstanceState != null) {
            isFinish = savedInstanceState.getBoolean(IS_FINISH_BUNDLE_KEY);
            isEmpty = savedInstanceState.getBoolean(IS_COCKTAIL_EMPRY);
        }

        dataStorage.subscribe(this);
        cocktail = dataStorage.getCocktailById(idDrink);
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

    private void render() {
        textView.setText(cocktail.getName());
        instructions.setText(cocktail.getInstruction());
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
        if(!isEmpty) {
            dataStorage.unsubscribe(this);
        }
        super.onDestroy();
    }

    @Override
    public void onDataUpdated(int dataType) {
        setCocktail(dataStorage.getCocktailById(idDrink));
    }
}
