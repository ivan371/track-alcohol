package nagaiko.track_alcohol.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nagaiko.track_alcohol.R;
import nagaiko.track_alcohol.models.Cocktail;

/**
 * Created by nagai on 14.11.2017.
 */

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.IngredientViewHolder>{

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        public TextView measureTextView;
        public TextView ingredientTextView;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            measureTextView = (TextView) itemView.findViewById(R.id.measure);
            ingredientTextView = (TextView) itemView.findViewById(R.id.ingredient);
        }
    }
    private Context mContext;
    private List<Cocktail.Ingredient> mIngredients;

    public IngredientRecyclerAdapter(Context context, List<Cocktail.Ingredient> ingredients) {
        mIngredients = ingredients;
        mContext = context;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View ingredientView = inflater.inflate(R.layout.item_ingredient_layout, parent, false);
        IngredientViewHolder viewHolder = new IngredientViewHolder(ingredientView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientRecyclerAdapter.IngredientViewHolder holder, int position) {
        Cocktail.Ingredient ingredient = mIngredients.get(position);
        TextView measureView = holder.measureTextView;
        measureView.setText(ingredient.getMeasure());
        TextView button = holder.ingredientTextView;
        button.setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    private Context getContext() {
        return mContext;
    }
}
