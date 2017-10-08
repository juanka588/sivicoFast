package com.rocket.sivico.Data;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocket.sivico.R;
import com.rocket.sivico.Utils;

/**
 * Created by JuanCamilo on 7/10/2017.
 */

public class ReportHolder extends RecyclerView.ViewHolder {
    public CardView cv;
    TextView title;
    TextView date;
    TextView hour;
    ImageView image;

    ReportHolder(View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.card_view_report);
        title = itemView.findViewById(R.id.report_title);
        date = itemView.findViewById(R.id.report_date);
        hour = itemView.findViewById(R.id.report_hour);
        image = itemView.findViewById(R.id.report_image);
    }


    public void bind(ReportTemp report) {
//        this.image.setBackgroundColor(Color.parseColor(report.getCategory().getColor()));
        this.title.setText(report.getDescription());
        this.date.setText(Utils.getFormatDate(Long.parseLong(report.getDate())));
        this.hour.setText(Utils.getHour(Long.parseLong(report.getDate())));
    }
}
