package com.rocket.sivico.GUI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewReportActivityFragment extends Fragment implements HandleNewLocation {

    static final int REQUEST_IMAGE_CAPTURE = 1001;
    static final int PLACE_PICKER_REQUEST = 1003;
    private static final int EDIT_IMAGE_REQUEST = 1004;
    private static final String TAG = NewReportActivityFragment.class.getSimpleName();
    private static final String IMAGE_URI = "image_uri";
    private static final String DESCRIPTION = "description";
    private static final String DATE = "date";
    private static final String HOUR = "hour";

    private ImageView preview;
    private FloatingActionButton edit;
    private TextView hourTx;
    private TextView dateTx;
    private TextView descriptionTx;

    private Uri imageUri;
    private String description;
    private String date;
    private String hour;
    private int mHour;
    private int mMinute;
    private int mYear;
    private int mMonth;
    private int mDay;
    private LatLng newPos;

    private GoogleMap mMap;
    private SivicoMenuActivity root;
    private SubCategory category;
    private MapView mapView;
    private DatabaseReference mReportRef = FirebaseDatabase.getInstance().getReference("reports");

    public NewReportActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_report, container, false);
        hourTx = view.findViewById(R.id.report_hour);
        dateTx = view.findViewById(R.id.report_date);
        descriptionTx = view.findViewById(R.id.report_description);
        edit = view.findViewById(R.id.edit_button);
        preview = view.findViewById(R.id.preview_image);
        Bundle bundle = getArguments();
        initControls(view);
        category = bundle.getParcelable(GlobalConfig.PARAM_SUBCATEGORY);
        associateCategory(category, view);
        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(IMAGE_URI);
            description = savedInstanceState.getString(DESCRIPTION);
            date = savedInstanceState.getString(DATE);
            hour = savedInstanceState.getString(HOUR);

            dateTx.setText(date);
            hourTx.setText(hour);
            descriptionTx.setText(description);
        }
        if (imageUri != null) {
            displayPhoto();
        }
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                initMap(googleMap);
            }
        });
        return view;
    }

    private void initMap(GoogleMap googleMap) {
        mMap = googleMap;
        if (getContext() != null) {
            MapsInitializer.initialize(getContext());
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mapView.onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI, imageUri);
        description = descriptionTx.getText().toString();
        outState.putString(DESCRIPTION, description);
        date = dateTx.getText().toString();
        outState.putString(DATE, date);
        hour = hourTx.getText().toString();
        outState.putString(HOUR, hour);
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
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                hourTx.setText(Utils.getHour(c.getTime().getTime() / 1000));
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
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                Date dateTime = c.getTime();
                                dateTx.setText(Utils.getFormatDate(dateTime.getTime() / 1000));

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
                    String key = mReportRef.push().getKey();
                    Report report = bindReportFromControls();
                    if (report == null) {
                        Log.e(TAG, "error binding report");
                        Toast.makeText(getContext(), "Reporte no creado fecha invalida",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    report.addEvidence(imageUri.toString());
                    mReportRef.getRef().child(key).updateChildren(report.toMap());
                    Intent resultIntent = new Intent(getActivity(), CategoryActivity.class);
                    getActivity().setResult(Activity.RESULT_OK, resultIntent);
                    getActivity().finish();
                } else {
                    Snackbar.make(view, getString(R.string.please_take_picture), Snackbar.LENGTH_LONG);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    editPhoto();
                } else {
                    Snackbar.make(view, getString(R.string.please_take_picture), Snackbar.LENGTH_LONG);
                }
            }
        });

        dateTx.setText(Utils.getFormatDate(new Date().getTime() / 1000));
        hourTx.setText(Utils.getHour(new Date().getTime() / 1000));
    }

    private Report bindReportFromControls() {
        Date dateTime;
        try {
            dateTime = Utils.sivicoDateAndHourFormat.parse(dateTx.getText().toString()
                    + " "
                    + hourTx.getText().toString());
            if (dateTime.after(new Date())) {
                return null;
            }
            String desc = descriptionTx.getText().toString();
            if (desc.isEmpty()) {
                desc = "Mi nuevo reporte de " + category.getName();
            }
            String lat = GlobalConfig.DEFAULT_LATITUDE;
            String lon = GlobalConfig.DEFAULT_LONGITUDE;
            if (newPos != null) {
                lat = String.valueOf(newPos.latitude);
                lon = String.valueOf(newPos.longitude);
            } else if (root.userPos != null) {
                lat = String.valueOf(root.userPos.latitude);
                lon = String.valueOf(root.userPos.longitude);
            }
            return new Report(
                    String.valueOf(dateTime.getTime() / 1000),
                    desc,
                    lat,
                    lon,
                    category.getName(),
                    root.firebaseUser.getUid(),
                    category.getColor()
            );
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e.fillInStackTrace());
            return null;
        }
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
                break;
            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getActivity());
                    String toastMsg = String.format("Place: %s", place.getName());
                    root.userPos = place.getLatLng();
                    root.located = false;
                    newPos = place.getLatLng();
                    handleNewLocation(null);
                    Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                }
                break;
            case EDIT_IMAGE_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        displayPhoto();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
                break;
        }
    }


    private void displayPhoto() {
        File photo = new File(imageUri.getPath());
        Picasso.with(getActivity()).load(photo).fit().into(preview);
        preview.setVisibility(View.VISIBLE);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bitmap bmp = BitmapFactory.decodeFile(photo.getAbsolutePath());
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        InputStream in = new ByteArrayInputStream(bos.toByteArray());
        try {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            FileOutputStream out = new FileOutputStream(new File(imageUri.getPath()));
            out.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editPhoto() {
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        editIntent.setDataAndType(imageUri, "image/*");
        editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(editIntent, null), EDIT_IMAGE_REQUEST);
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
        if (root.located) {
            return;
        }
        root.located = true;
        if (mMap != null) {
            mMap.clear();
            MarkerOptions markerOptions = new MarkerOptions().position(root.userPos)
                    .title("Sitio de la denuncia").snippet("Editar");
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(root.userPos));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    callPlacePicker();
                    return false;
                }
            });
        }
    }

    private void callPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

}
