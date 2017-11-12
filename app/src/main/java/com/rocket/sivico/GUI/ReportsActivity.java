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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rocket.sivico.Adapters.ReportHolder;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
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
    private FirebaseRecyclerAdapter<Report, ReportHolder> mAdapter;
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
        mReportRef.keepSynced(true);
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


    protected FirebaseRecyclerAdapter<Report, ReportHolder> getAdapter() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.sign_out), Toast.LENGTH_LONG).show();
            finish();
        }
        String id = mAuth.getCurrentUser().getUid();
        Query query = mReportRef.orderByChild("owner").startAt(id).endAt(id);
        FirebaseRecyclerOptions<Report> options =
                new FirebaseRecyclerOptions.Builder<Report>()
                        .setQuery(query, Report.class)
                        .build();
        return new FirebaseRecyclerAdapter<Report, ReportHolder>(
                options) {
            @Override
            public ReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.report_item_view, parent, false);
                return new ReportHolder(view);
            }

            @Override
            public void onDataChanged() {
                // If there are no chat messages, show a view that invites the user to add a message.
                mEmptyListMessage.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            protected void onBindViewHolder(ReportHolder holder, int position, final Report report) {
                holder.bind(report);
                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ReportsActivity.this.onReportClick(report);
                    }
                });
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
