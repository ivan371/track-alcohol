package nagaiko.track_alcohol.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import nagaiko.track_alcohol.R;


/**
 * Created by Konstantin on 24.10.2017.
 */

public class ListViewHolder extends RecyclerView.ViewHolder {

    public void setName(String text) {
        name.setText(text);
    }
//    public void setIngredient(String text){ingredient.setText("Ingredient: " + text + ",...");}

    private TextView name;
//    private TextView ingredient;

    public ListViewHolder(View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.name);
//        ingredient = (TextView)itemView.findViewById(R.id.ingredient);
    }

}