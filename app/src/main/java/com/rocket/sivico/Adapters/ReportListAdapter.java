package com.rocket.sivico.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocket.sivico.Data.Report;
import com.rocket.sivico.Interfaces.OnReportClick;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;

import java.util.List;

/**
 * Created by JuanCamilo on 15/07/2015.
 */
public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.reportVH> {

    private List<Report> reports;
    private OnReportClick callBack;


    @Override
    public reportVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item_view, parent, false);
        reportVH pvh = new reportVH(v);
        return pvh;
    }

    @Override
    public void onViewRecycled(reportVH holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(final reportVH holder, int i) {
        Report report = reports.get(i);
        //TODO: bind color type
        holder.title.setText(report.getDescription());
        holder.date.setText(Utils.getFormatDate(report.getTime()));
        holder.hour.setText(Utils.getHour(report.getTime()));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public ReportListAdapter(List<Report> participants, OnReportClick callBack) {
        this.reports = participants;
        this.callBack = callBack;
    }

    public class reportVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView title;
        TextView date;
        TextView hour;

        reportVH(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card_view_report);
            title = itemView.findViewById(R.id.report_title);
            date = itemView.findViewById(R.id.report_date);
            hour = itemView.findViewById(R.id.report_hour);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Report report = reports.get(getAdapterPosition());
            callBack.onReportClick(report);
        }

    }
}
