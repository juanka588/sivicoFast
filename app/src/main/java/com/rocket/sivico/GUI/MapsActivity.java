package com.rocket.sivico.GUI;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.Data.ReportRender;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.R;

public class MapsActivity extends SivicoMenuActivity implements OnMapReadyCallback,
        ClusterManager.OnClusterClickListener<Report>,
        ClusterManager.OnClusterInfoWindowClickListener<Report>,
        ClusterManager.OnClusterItemClickListener<Report>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Report> {


    private GoogleMap mMap;
    private ClusterManager<Report> mClusterManager;
    private DatabaseReference mReportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        loadActionBar();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, CategoryActivity.class));
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setUpClusters();
        mReportRef = FirebaseDatabase.getInstance()
                .getReference("reports")
                .orderByChild("date").limitToLast(50)
                .getRef();
        mReportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Report report = new Report(ds);
                    mClusterManager.addItem(report);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpClusters() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mClusterManager.setRenderer(new ReportRender(this, mMap, mClusterManager));
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                mClusterManager.onInfoWindowClick(marker);
            }
        });
    }

    @Override
    public void handleNewLocation(Location location) {
        super.handleNewLocation(location);
        if (!located) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        }
        located = true;
    }

    @Override
    public boolean onClusterClick(Cluster<Report> cluster) {
        return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Report> cluster) {

    }

    @Override
    public boolean onClusterItemClick(Report report) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Report report) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(GlobalConfig.PARAM_REPORT, report);
        startActivity(intent);
    }
}
