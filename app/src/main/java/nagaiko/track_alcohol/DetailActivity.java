package nagaiko.track_alcohol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import nagaiko.track_alcohol.models.Cocktail;

public class DetailActivity extends AppCompatActivity {

    private DataStorage dataStorage = DataStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int defaultValue = 0;
        TextView textView;
        setContentView(R.layout.activity_detail);
        textView= (TextView) findViewById(R.id.textView);
        Cocktail[] data = (Cocktail[])dataStorage.getData(DataStorage.COCKTAIL_FILTERED_LIST);
        int position = getIntent().getIntExtra("position", defaultValue);
        textView.setText(data[position].getName());
    }
}
