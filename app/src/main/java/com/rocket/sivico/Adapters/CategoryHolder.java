package com.rocket.sivico.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocket.sivico.Data.Category;
import com.rocket.sivico.R;

/**
 * Created by JuanCamilo on 11/10/2017.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {
    public CardView cv;
    LinearLayout ll;
    TextView title;
    TextView number;
    ImageView image;

    CategoryHolder(View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.card_view_category);
        ll = itemView.findViewById(R.id.category_background);
        title = itemView.findViewById(R.id.category_title);
        number = itemView.findViewById(R.id.category_number);
        image = itemView.findViewById(R.id.category_image);
    }

    public void bind(Category category) {
        this.title.setText(category.getName());
//            Picasso.with(this.cv.getContext()).load(category.getIcon()).into(this.image);

    }

}
