package com.rocket.sivico.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocket.sivico.Data.SubCategory;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;

/**
 * Created by JuanCamilo on 11/10/2017.
 */

public class SubCategoryHolder extends RecyclerView.ViewHolder {
    public CardView cv;
    LinearLayout ll;
    TextView title;
    TextView number;

    SubCategoryHolder(View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.card_view_category);
        ll = itemView.findViewById(R.id.category_background);
        title = itemView.findViewById(R.id.category_title);
        number = itemView.findViewById(R.id.category_number);
    }

    public void bind(SubCategory subCategory, int position) {
        this.title.setText(Utils.formatString(subCategory.getName()));
        this.number.setText(String.valueOf(position) + ".");
    }

}

