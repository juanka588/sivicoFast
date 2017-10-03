package com.rocket.sivico.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocket.sivico.Data.Category;
import com.rocket.sivico.Interfaces.OnCategoryClick;
import com.rocket.sivico.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JuanCamilo on 15/07/2015.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    private List<Category> categories;
    private OnCategoryClick callBack;
    private int layout;


    @Override
    public CategoryVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        CategoryVH cVH = new CategoryVH(v);
        return cVH;
    }

    @Override
    public void onViewRecycled(CategoryVH holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final CategoryVH holder, int i) {
        Category category = categories.get(i);
        holder.title.setText(category.getTitle());
        if (layout == R.layout.category_item_view) {
            holder.number.setText((i + 1) + ".");
        } else if (layout == R.layout.category_main_item_view) {
//            Picasso.with(holder.cv.getContext()).load(category.getTitle()).into(holder.image);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public CategoryAdapter(int layout, List<Category> categories, OnCategoryClick callBack) {
        this.categories = categories;
        this.callBack = callBack;
        this.layout = layout;
    }

    public class CategoryVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        LinearLayout ll;
        TextView title;
        TextView number;
        ImageView image;

        CategoryVH(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view_category);
            ll = itemView.findViewById(R.id.category_background);
            title = itemView.findViewById(R.id.category_title);
            number = itemView.findViewById(R.id.category_number);
            image = itemView.findViewById(R.id.category_image);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Category category = categories.get(getAdapterPosition());
            callBack.onCategoryClick(category);
        }
    }
}
