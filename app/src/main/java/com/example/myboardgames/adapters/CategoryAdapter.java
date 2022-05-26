package com.example.myboardgames.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myboardgames.dialogs.CategoryDialog;
import com.example.myboardgames.helpers.GamesProcessor;
import com.example.myboardgames.R;
import com.example.myboardgames.helpers.Utils;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private List<String> categories;
    private boolean[] isHidden;
    private final LayoutInflater inflater;
    private Context context;

    /**
     * public constructor with parameters
     *
     * @param context
     * @param categories
     */
    public CategoryAdapter(Context context, List<String> categories) {
        this.categories = categories;
        this.isHidden = new boolean[categories.size()];
        this.context = context;
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

        holder.tvCategoryName.setText(category);

        if (category.equals(GamesProcessor.getCategories().get(0))) {
            holder.ibEditButton.setVisibility(View.INVISIBLE);
            holder.ibRemoveCategory.setVisibility(View.INVISIBLE);
        }
        else {
            holder.ibEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.dialog = new CategoryDialog(context, R.string.dialog_title_edit,
                            R.string.dialog_category_name_hint_edit, R.string.dialog_button_edit,
                            category, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String categoryName = holder.dialog.getCategoryName();
                            if(!Utils.validateName(categoryName))
                                Toast.makeText(context, "Введіть непорожню назву категорії!", Toast.LENGTH_LONG).show();
                            else if (category.equals(categoryName)) {
                                Toast.makeText(context, "Назву категорії не було змінено", Toast.LENGTH_LONG).show();
                                holder.dialog.dismiss();
                            }
                            else if (!(categoryName.toLowerCase().equals(category.toLowerCase())) && GamesProcessor.categoryNameAlreadyExists(categoryName))
                                Toast.makeText(context, "Така категорія вже існує!", Toast.LENGTH_LONG).show();
                            else{
                                GamesProcessor.editCategory(context, position, category, categoryName);
                                GamesProcessor.saveCategories(context);
                                CategoryAdapter.this.notifyItemChanged(position);
                                holder.dialog.dismiss();
                            }
                        }
                    });
                    holder.dialog.show();
                }
            });

            holder.ibRemoveCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GamesProcessor.deleteCategory(context, category);
                    GamesProcessor.saveCategories(context);
                    CategoryAdapter.this.notifyDataSetChanged();
                }
            });
        }
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
        public CategoryDialog dialog;

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
