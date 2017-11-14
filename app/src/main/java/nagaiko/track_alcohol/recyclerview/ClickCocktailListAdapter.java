package nagaiko.track_alcohol.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by Konstantin on 24.10.2017.
 */

public class ClickCocktailListAdapter extends ListCocktailListAdapter implements View.OnClickListener {
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private final OnItemClickListener mClickListener;

    public ClickCocktailListAdapter(LayoutInflater inflater, ArrayList<Cocktail> data, OnItemClickListener listener) {
        super(inflater, data);
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