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
import nagaiko.track_alcohol.api.ImageResponse;
import nagaiko.track_alcohol.api.Response;
import nagaiko.track_alcohol.models.Cocktail;

import static nagaiko.track_alcohol.api.ApiResponseTypes.COCKTAIL_THUMB;

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

    public class ListViewHolder extends RecyclerView.ViewHolder implements DataStorage.Subscriber {

        public void setName(String text) {
            name.setText(text);
        }
        public void setImg(Bitmap thumb) {
            if (thumb != null) {
                img.setImageBitmap(thumb);
            }
        }
        public void setUrl(String url) {
            this.url = url;
        }

        private TextView name;
        public ImageView img;
        private String url;

        public ListViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            img = itemView.findViewById(R.id.coctail);
        }

        @Override
        public void onDataLoaded(int type, Response response) {
            if (type == COCKTAIL_THUMB) {
                ImageResponse imageResponse = (ImageResponse)response.content;
                if (this.url != null && this.url.equals(imageResponse.url)) {
                    setImg(imageResponse.bm);
                }
            }
        }

        @Override
        public void onDataLoadFailed() {

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
        String url = data.get(position).getThumb();
        holder.setUrl(url);
        holder.setImg(dataStorage.getCocktailThumb(holder, url, holder.img));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setNewData(List<Cocktail> cocktails) {
        data = cocktails;
        notifyDataSetChanged();
    }
}