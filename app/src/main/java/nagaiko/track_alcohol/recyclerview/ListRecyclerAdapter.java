package nagaiko.track_alcohol.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import nagaiko.track_alcohol.R;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder> {

    private final WeakReference<LayoutInflater> mInflater;
    public final String[] names;
//    public final String[] ingredient;

    public ListRecyclerAdapter(LayoutInflater inflater, String[] names) {
        mInflater = new WeakReference<LayoutInflater>(inflater);
        this.names = names;
//        this.ingredient = ingredient;
    }

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

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = mInflater.get();
        if (inflater != null) {
            return new ListViewHolder(inflater.inflate(R.layout.item_list_layout, parent, false));
        }
        else {
            throw new RuntimeException("Oooops, looks like activity is dead");
        }
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        holder.setName(names[position]);
//        holder.setIngredient(ingredient[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}