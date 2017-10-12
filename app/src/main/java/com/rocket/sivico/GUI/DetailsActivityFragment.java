package com.rocket.sivico.GUI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle bundle = getArguments();
        Report report = bundle.getParcelable(GlobalConfig.PARAM_REPORT);
        bindReportData(inflate, report);
        manageToolbar(inflate, report);
        return inflate;
    }

    private void bindReportData(View inflate, Report report) {

    }

    private void manageToolbar(View view, Report report) {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_details));
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor(report.getColor()));
    }
}
