package com.rocket.sivico.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.rocket.sivico.Adapters.ReportListAdapter;
import com.rocket.sivico.Data.Category;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.Interfaces.OnReportClick;
import com.rocket.sivico.R;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends SivicoMenuActivity implements OnReportClick {

    private static final String TAG = ReportsActivity.class.getSimpleName();
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        loadActionBar();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportsActivity.this, CategoryActivity.class));
            }
        });

        loadReports();
    }

    private void loadReports() {
        RecyclerView reportList = findViewById(R.id.reports_list);
        List<Category> categories = GlobalConfig.initCategories();
        List<Report> reports = new ArrayList<>();
        reports.add(new Report("1", "mucho dolor", categories.get(0).getChildren().get(0)));
        reports.add(new Report("2", "mucho ardor", categories.get(0).getChildren().get(1)));
        reports.add(new Report("6", "migraña", categories.get(1).getChildren().get(0)));
        reports.add(new Report("8", "mortalidad infantil", categories.get(2).getChildren().get(0)));
        reports.add(new Report("dskjsfhd", "perros salvajes", categories.get(3).getChildren().get(0)));
        reports.add(new Report("9", "mosquitos", categories.get(0).getChildren().get(3)));

        reportList.setAdapter(new ReportListAdapter(reports, this));
        reportList.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(new int[]{
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorAccent
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
    }

    private void refreshData() {
        refreshLayout.setRefreshing(false);
    }


    @Override
    public void onReportClick(Report report) {
        Log.i(TAG, report.getDescription());
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(GlobalConfig.PARAM_REPORT, report);
        startActivity(intent);
    }
}
