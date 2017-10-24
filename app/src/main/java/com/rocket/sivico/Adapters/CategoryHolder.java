package com.rocket.sivico.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocket.sivico.Data.Category;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;

/**
 * Created by JuanCamilo on 11/10/2017.
 */

public class CategoryHolder extends RecyclerView.ViewHolder {
    public CardView cv;
    LinearLayout ll;
    TextView title;
    TextView description;
    public Button next;
    public Button prev;
    ImageView image;

    CategoryHolder(View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.card_view_category);
        ll = itemView.findViewById(R.id.category_background);
        title = itemView.findViewById(R.id.category_title);
        description = itemView.findViewById(R.id.category_description);
        image = itemView.findViewById(R.id.category_image);
        next = itemView.findViewById(R.id.next_button);
        prev = itemView.findViewById(R.id.prev_button);
    }

    public void bind(Category category) {
        this.title.setText(Utils.formatString(category.getName()));
        this.description.setText(Utils.formatString(category.getDescription()));
        String resName = category.getIcon();
        Context context = this.cv.getContext();
        int icon = R.drawable.ic_nature;
        if (resName.startsWith("local//")) {
            icon = context.getResources().getIdentifier("drawable/" + resName.replaceAll("local//", ""), null,
                    context.getPackageName());
            if (icon == 0) {
                icon = R.drawable.ic_nature;
            }
        }
        image.setImageResource(icon);
//        Picasso.with(this.cv.getContext()).load(icon).fit().into(this.image);
    }

}

