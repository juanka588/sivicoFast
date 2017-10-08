package com.rocket.sivico.GUI;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.Data.ReportHolder;
import com.rocket.sivico.Data.ReportTemp;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.Interfaces.OnReportClick;
import com.rocket.sivico.R;

public class ReportsActivity extends SivicoMenuActivity implements OnReportClick, LifecycleRegistryOwner {

    private static final String TAG = ReportsActivity.class.getSimpleName();
    private SwipeRefreshLayout refreshLayout;
    private FirebaseAuth mAuth;
    protected DatabaseReference mReportRef;

    private final LifecycleRegistry mRegistry = new LifecycleRegistry(this);

    private LinearLayoutManager mManager;
    private FirebaseRecyclerAdapter<ReportTemp, ReportHolder> mAdapter;
    protected TextView mEmptyListMessage;
    private RecyclerView reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        mEmptyListMessage = findViewById(R.id.emptyTextView);
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
        reportList = findViewById(R.id.reports_list);
        mAuth = FirebaseAuth.getInstance();

        mReportRef = FirebaseDatabase.getInstance().getReference().child("reports");
        mManager = new LinearLayoutManager(this);
        reportList.setLayoutManager(mManager);
        reportList.setHasFixedSize(true);
        if (isSignedIn()) {
            attachRecyclerViewAdapter();
        }

    }

    private void attachRecyclerViewAdapter() {
        mAdapter = getAdapter();

        // Scroll to bottom on new messages
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mManager.smoothScrollToPosition(reportList, null, mAdapter.getItemCount());
            }
        });

        reportList.setAdapter(mAdapter);


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

    private boolean isSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return mRegistry;
    }

    protected FirebaseRecyclerAdapter<ReportTemp, ReportHolder> getAdapter() {
        Query lastFifty = mReportRef.limitToLast(50);
        return new FirebaseRecyclerAdapter<ReportTemp, ReportHolder>(
                ReportTemp.class,
                R.layout.report_item_view,
                ReportHolder.class,
                lastFifty,
                this) {
            @Override
            public void populateViewHolder(ReportHolder holder, ReportTemp report, int position) {
                holder.bind(report);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                callBack.onReportClick(report);
                    }
                });
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        };
    }
}
