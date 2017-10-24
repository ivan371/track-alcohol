package nagaiko.track_alcohol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity implements
        ClickRecyclerAdapter.OnItemClickListener{

    public final String LOG_TAG = this.getClass().getSimpleName();

    int currentVisiblePosition = 0;
    private static final String VISIBLE_POSITION = "position";

    private RecyclerView recyclerView;
    private TextView textView;

    private static String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentVisiblePosition = savedInstanceState.getInt(VISIBLE_POSITION);
        }
        Log.d(LOG_TAG, Integer.toString(currentVisiblePosition));

        Intent intent = getIntent();

        recyclerView = new RecyclerView(this);

        names = Arrays.copyOf(intent.getStringArrayExtra("names"), intent.getStringArrayExtra("names").length);

        recyclerView.setAdapter(new ClickRecyclerAdapter(getLayoutInflater(),names, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setHasFixedSize(true);
        setContentView(recyclerView);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, names[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart(){
        super.onStart();

        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(LOG_TAG, "onPause");

    }

    @Override
    protected void onStop(){
        super.onStop();

        Log.d(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(LOG_TAG, Integer.toString(currentVisiblePosition));

        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        outState.putInt(VISIBLE_POSITION, currentVisiblePosition);
    }

}
