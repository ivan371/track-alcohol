package nagaiko.track_alcohol;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class ClickRecyclerAdapter extends ListRecyclerAdapter implements View.OnClickListener {
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private final OnItemClickListener mClickListener;

    public ClickRecyclerAdapter(LayoutInflater inflater, String[] names, OnItemClickListener listener) {
        super(inflater, names);
        mClickListener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListViewHolder holder = super.onCreateViewHolder(parent, viewType);
        holder.itemView.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setTag(position);
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer)v.getTag();
        mClickListener.onItemClick(v, position);
    }
}