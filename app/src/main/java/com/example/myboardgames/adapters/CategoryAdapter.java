package com.example.myboardgames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.ui.CategoriesActivity;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<String> categories;
    private boolean[] isHidden;
    private OnCategoryClickListener onCategoryClickListener;
    private final LayoutInflater inflater;
    private Context context;

    /**
     * public interface is used to set action when the category item is clicked
     */
    public interface OnCategoryClickListener {
        /**
         * method is used to react to the click on the category item
         *
         * @param category
         * @param position
         */
        void onCategoryClicked(String category, int position);
    }

    /**
     * public constructor with parameters
     *
     * @param context
     * @param categories
     * @param onClickListener
     */
    public CategoryAdapter(Context context, List<String> categories, OnCategoryClickListener onClickListener) {
        this.categories = categories;
        this.isHidden = new boolean[categories.size()];
        this.context = context;
        onCategoryClickListener = onClickListener;
        inflater = LayoutInflater.from(context);
    }

    /**
     * method is used to set layout to the category item
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.categories_item, parent, false);
        return new CategoryViewHolder(view);
    }

    /**
     * method is used to set data to the category item
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        if (categories.size() != isHidden.length) {
            isHidden = new boolean[categories.size()];
        }
        if (isHidden[position]) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(getZeroLayoutParams());
            return;
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(getDefaultLayoutParams());
        }

        String category = categories.get(position);

        holder.ibRemoveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GamesProcessor.deleteCategory(context, category);
                GamesProcessor.saveCategories(context);
                CategoryAdapter.this.notifyDataSetChanged();
            }
        });

        //ButtonsActions.favoriteAction(category, holder.ibFavoriteButton, context);
        //ButtonsActions.chooseAction(category, holder.ibCheckcategory, context, null, null);

        holder.tvCategoryName.setText(category);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryClickListener.onCategoryClicked(category, position);
            }
        });
    }

    /**
     * method is used to get size of the categories list
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return categories.size();
    }

    private static RecyclerView.LayoutParams getDefaultLayoutParams() {
        RecyclerView.LayoutParams res = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        //TODO Звідки такі числа???
        res.topMargin = 25;
        res.rightMargin = 25;
        res.leftMargin = 25;
        return res;
    }

    private static RecyclerView.LayoutParams getZeroLayoutParams() {
        return new RecyclerView.LayoutParams(0, 0);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCategoryName;
        public ImageButton ibEditButton, ibRemoveCategory;

        /**
         * public constructor with parameters
         *
         * @param itemView
         */
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        /**
         * method is used to initialise views of the view holder
         *
         * @param itemView
         */
        private void init(View itemView) {
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ibEditButton = itemView.findViewById(R.id.ibEditButton);
            ibRemoveCategory = itemView.findViewById(R.id.ibRemoveCategory);
        }
    }
}
