package com.rocket.sivico.GUI;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private GoogleMap mMap;
    private MapView mapView;

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle bundle = getArguments();
        final Report report = bundle.getParcelable(GlobalConfig.PARAM_REPORT);
        mapView = inflate.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        bindReportData(inflate, report);
        manageToolbar(inflate, report);
        return inflate;
    }

    private void bindReportData(View inflate, final Report report) {
        TextView date = inflate.findViewById(R.id.report_date);
        date.setText(Utils.getFormatDate(report.getDate()));

        TextView score = inflate.findViewById(R.id.user_score_text);
        score.setText(String.valueOf(report.getScore()));

        TextView description = inflate.findViewById(R.id.report_description);
        description.setText(report.getDescription());

        TextView hour = inflate.findViewById(R.id.report_hour);
        hour.setText(Utils.getHour(report.getDate()));
        ImageView image = inflate.findViewById(R.id.report_image);
        Picasso.with(getContext())
                .load(report.getEvidencesList().get(0).getImage())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.city_side)
                .into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPhoto(Uri.parse(report.getEvidencesList().get(0).getImage()));
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (getContext() != null) {
                    MapsInitializer.initialize(getContext());
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                LatLng position = report.getPosition();
                mMap.addMarker(new MarkerOptions().position(position).title("Sitio de la denuncia"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
                mapView.onResume();
            }
        });
    }

    private void manageToolbar(View view, Report report) {
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_details));
        collapsingToolbarLayout.setContentScrimColor(Color.parseColor(report.getColor()));
    }

    private void showPhoto(Uri photoUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(photoUri, "image/*");
        startActivity(intent);
    }
}
