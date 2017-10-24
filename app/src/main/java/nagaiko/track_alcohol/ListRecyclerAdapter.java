package nagaiko.track_alcohol;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private final WeakReference<LayoutInflater> mInflater;
    public final String[] names;

    public ListRecyclerAdapter(LayoutInflater inflater, String[] names) {
        mInflater = new WeakReference<LayoutInflater>(inflater);
        this.names = names;
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