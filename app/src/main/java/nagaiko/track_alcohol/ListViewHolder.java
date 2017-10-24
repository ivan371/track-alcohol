package nagaiko.track_alcohol;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;



/**
 * Created by Konstantin on 24.10.2017.
 */

public class ListViewHolder extends RecyclerView.ViewHolder {

    public void setName(String text) {
        name.setText(text);
    }

    private TextView name;

    public ListViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.name);
    }

}