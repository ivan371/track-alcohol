package nagaiko.track_alcohol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Serializable result = getIntent().getStringExtra("result");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(result.toString());
    }
}
