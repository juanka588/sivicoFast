package com.rocket.sivico.Adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocket.sivico.Data.Report;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;
import com.squareup.picasso.Picasso;

/**
 * Created by JuanCamilo on 7/10/2017.
 */

public class ReportHolder extends RecyclerView.ViewHolder {
    public CardView cv;
    TextView title;
    TextView date;
    TextView hour;
    ImageView image;

    public ReportHolder(View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.card_view_report);
        title = itemView.findViewById(R.id.report_title);
        date = itemView.findViewById(R.id.report_date);
        hour = itemView.findViewById(R.id.report_hour);
        image = itemView.findViewById(R.id.report_image);
    }

    public void bind(Report report) {
        this.title.setText(Utils.formatString(report.getDescription()));
        this.date.setText(Utils.getFormatDate(Long.parseLong(report.getDate())));
        this.hour.setText(Utils.getHour(Long.parseLong(report.getDate())));
        this.image.setBackgroundColor(Color.parseColor(report.getColor()));
        Log.e("bind holder", report.getEvidence().get("img1").toString());
        Picasso.with(this.cv.getContext())
                .load(report.getEvidence().get("img1").toString())
                .fit()
                .placeholder(R.drawable.ic_file_upload)
                .into(this.image);
    }
}
