package nagaiko.track_alcohol.recyclerview;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import nagaiko.track_alcohol.DataStorage;
import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class ListCocktailListAdapter extends RecyclerView.Adapter<ListCocktailListAdapter.ListViewHolder> {

    private final WeakReference<LayoutInflater> mInflater;
    protected List<Cocktail> data;
    private DataStorage dataStorage = DataStorage.getInstance();

    public ListCocktailListAdapter(LayoutInflater inflater, List<Cocktail> data) {
        mInflater = new WeakReference<LayoutInflater>(inflater);
        this.data = data;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        public void setName(String text) {
            name.setText(text);
        }
        public void setImg(Bitmap thumb) {
            if (thumb != null) {
                img.setImageBitmap(thumb);
            }
        }

        private TextView name;
        private ImageView img;

        public ListViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.coctail);
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
        holder.setName(data.get(position).getName());
        int id = data.get(position).getId();
//        holder.setImg(dataStorage.getCocktailThumb(id));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setNewData(ArrayList<Cocktail> cocktails) {
        data = cocktails;
        notifyDataSetChanged();
    }
}