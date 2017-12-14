package com.rocket.sivico.Data;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by Camilo on 12/12/2017.
 */

public class ReportRender extends DefaultClusterRenderer<Report> {

    public ReportRender(Activity context, GoogleMap map, ClusterManager<Report> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Report report, MarkerOptions markerOptions) {
        markerOptions
                .title(report.getDescription())
                .snippet(report.getCategory());
    }
}