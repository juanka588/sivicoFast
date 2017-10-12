package com.rocket.sivico.GUI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.SubCategory;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewReportActivityFragment extends Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = NewReportActivityFragment.class.getSimpleName();
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 102;
    ImageView preview;
    private Uri imageUri;
    private int mHour;
    private int mMinute;
    private TextView hour;
    private TextView date;
    private int mYear;
    private int mMonth;
    private int mDay;
    private GoogleApiClient mGoogleClient;
    private LatLng userPos;
    private boolean located;
    private LocationRequest mLocReq;
    private GoogleMap mMap;


    public NewReportActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_report, container, false);
        hour = view.findViewById(R.id.report_hour);
        date = view.findViewById(R.id.report_date);
        preview = view.findViewById(R.id.preview_image);
        Bundle bundle = getArguments();
        initControls(view);
        SubCategory category = bundle.getParcelable(GlobalConfig.PARAM_CATEGORY);
        associateCategory(category, view);
        if (imageUri != null) {
            displayPhoto();
        }

        mGoogleClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocReq = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        mGoogleClient.connect();


        MapView mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(getContext());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        });

        return view;
    }

    private void associateCategory(SubCategory category, View view) {
        TextView cat = view.findViewById(R.id.report_category);
        cat.setText(category.getName());
    }

    private void initControls(View view) {
        Button addEvidence = view.findViewById(R.id.add_evidence_button);
        addEvidence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/" + GlobalConfig.SIVICO_DIR);
                folder.mkdirs();
                File photo = new File(folder.toString(), Utils.getPhotoName());
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                NewReportActivityFragment.this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        Button editHour = view.findViewById(R.id.edit_hour_button);
        editHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                hour.setText(hourOfDay + ":" + minute + " " + c.get(Calendar.AM_PM));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        Button editDate = view.findViewById(R.id.edit_date_button);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.add_report_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
        if (location != null) {
            handleNewLocation(location);

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocReq, new com.google.android.gms.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                handleNewLocation(location);
            }
        });
    }


    private void handleNewLocation(Location location) {
        if (mMap == null) {
            located = false;
        }
        if (located) {
            return;
        }
        Log.i(TAG, "New Location");
        located = true;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        final LatLng latLng = new LatLng(latitude, longitude);
        userPos = latLng;
        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userPos).title("Sitio de la denuncia"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userPos));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        }
        Log.i(TAG, location.toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
//                        InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
                        displayPhoto();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "ConnectionSuspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "ConnectionFailed!!");
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void displayPhoto() {
        File photo = new File(imageUri.getPath());
        Picasso.with(getActivity()).load(photo).into(preview);
        preview.setVisibility(View.VISIBLE);
    }


}
