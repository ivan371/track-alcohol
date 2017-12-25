package nagaiko.track_alcohol.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class ListCategoryListAdapter extends RecyclerView.Adapter<ListCategoryListAdapter.ListViewHolder> {

    private final WeakReference<LayoutInflater> mInflater;
    public List<String> data;

    public ListCategoryListAdapter(LayoutInflater inflater, List<String> data) {
        mInflater = new WeakReference<LayoutInflater>(inflater);
        this.data = data;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        public void setName(String text) {
            name.setText(text);
        }

        private TextView name;

        public ListViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
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
        holder.setName(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void setNewData(List<String> categories) {
        data = categories;
        notifyDataSetChanged();
    }
}