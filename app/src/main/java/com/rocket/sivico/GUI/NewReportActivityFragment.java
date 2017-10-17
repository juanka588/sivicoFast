package com.rocket.sivico.GUI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.Data.SubCategory;
import com.rocket.sivico.Interfaces.HandleNewLocation;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewReportActivityFragment extends Fragment implements HandleNewLocation {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = NewReportActivityFragment.class.getSimpleName();

    ImageView preview;
    private Uri imageUri;
    private int mHour;
    private int mMinute;
    private TextView hour;
    private TextView date;
    private TextView description;
    private int mYear;
    private int mMonth;
    private int mDay;
    private GoogleMap mMap;
    private SivicoMenuActivity root;
    private StorageReference mImageRef;
    private SubCategory category;
    private DatabaseReference mReportRef = FirebaseDatabase.getInstance().getReference("reports");

    public NewReportActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_report, container, false);
        hour = view.findViewById(R.id.report_hour);
        date = view.findViewById(R.id.report_date);
        description = view.findViewById(R.id.report_description);
        preview = view.findViewById(R.id.preview_image);
        Bundle bundle = getArguments();
        initControls(view);
        category = bundle.getParcelable(GlobalConfig.PARAM_CATEGORY);
        associateCategory(category, view);
        if (imageUri != null) {
            displayPhoto();
        }
        final MapView mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                MapsInitializer.initialize(getContext());
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mapView.onResume();
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
                mHour = c.get(Calendar.HOUR);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                hour.setText(hourOfDay + ":" + minute + " " + (c.get(Calendar.AM_PM) == 0 ? "AM" : "PM"));
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
                if (imageUri != null) {
                    uploadPhoto(imageUri);
                    String key = mReportRef.push().getKey();
                    Report report = bindReportFromControls();
                    mReportRef.getRef().child(key).updateChildren(report.toMap());
                }
            }
        });
    }

    private Report bindReportFromControls() {
        Date dateTime = new Date();
        try {
            dateTime = Utils.sivicoDateFormat.parse(date.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Report newReport = new Report(
                String.valueOf(dateTime.getTime() / 1000),
                description.getText().toString(),
                String.valueOf(root.userPos.latitude),
                String.valueOf(root.userPos.longitude),
                category.getName(),
                root.firebaseUser.getUid(),
                category.getColor()
        );
        return newReport;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        displayPhoto();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }


    private void displayPhoto() {
        File photo = new File(imageUri.getPath());
        Picasso.with(getActivity()).load(photo).into(preview);
        preview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        root = (SivicoMenuActivity) activity;
        root.locationCallback = this;
    }


    @Override
    public void handleNewLocation(Location location) {
        if (mMap == null) {
            root.located = false;
        }
        root.handleNewLocation(location);
        if (mMap != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(root.userPos).title("Sitio de la denuncia"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(root.userPos));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        }
        Log.i(TAG, location.toString());
    }

    private void uploadPhoto(Uri uri) {
        // Reset UI
        hideDownloadUI();
        Toast.makeText(getContext(), "Uploading...", Toast.LENGTH_SHORT).show();

        // Upload to Cloud Storage
        String uuid = UUID.randomUUID().toString();
        mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(uri)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //noinspection LogConditional
                        Log.d(TAG, "uploadPhoto:onSuccess:" +
                                taskSnapshot.getMetadata().getReference().getPath());
                        Toast.makeText(getContext(), "Image uploaded",
                                Toast.LENGTH_SHORT).show();

                        showDownloadUI();
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "uploadPhoto:onError", e);
                        Toast.makeText(getContext(), "Upload failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDownloadUI() {
    }

    private void hideDownloadUI() {

    }
}
