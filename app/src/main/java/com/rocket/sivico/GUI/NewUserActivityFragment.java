package com.rocket.sivico.GUI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.Interfaces.OnEditUser;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewUserActivityFragment extends Fragment {

    private static final String TAG = NewUserActivityFragment.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private ImageView userPhoto;
    private EditText userIdNumber;
    private EditText userName;
    private EditText userPhone;
    private Button userNewPic;
    private Button userNewBirthDay;
    private TextView userBirthday;
    private RadioGroup gender;
    private RadioButton maleRB;
    private RadioButton femaleRB;
    private EditText region;
    private EditText neighborhood;
    private Switch anonymousSwitch;

    private OnEditUser callback;
    private FloatingActionButton saveUser;
    private StorageReference mImageRef;
    private Uri imageUri;
    private User editedUser;
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean genderSelected;
    private boolean isAnonymous;

    public NewUserActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);
        bindControls(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            editedUser = bundle.getParcelable(GlobalConfig.PARAM_USER);
            bindUser(editedUser);
        } else {
            editedUser = null;
            bindUser(FirebaseAuth.getInstance().getCurrentUser());
        }
        return view;
    }

    private void bindControls(View view) {
        anonymousSwitch = view.findViewById(R.id.switchAnonymous);
        userPhoto = view.findViewById(R.id.user_photo);
        userNewPic = view.findViewById(R.id.take_user_photo);
        userName = view.findViewById(R.id.user_name);
        userBirthday = view.findViewById(R.id.user_birthday);
        userNewBirthDay = view.findViewById(R.id.select_date);
        gender = view.findViewById(R.id.user_gender_group);
        userIdNumber = view.findViewById(R.id.user_id_number);
        userPhone = view.findViewById(R.id.user_phone);
        region = view.findViewById(R.id.user_region);
        neighborhood = view.findViewById(R.id.user_neighborhood);
        saveUser = view.findViewById(R.id.fab);
        maleRB = view.findViewById(R.id.male);
        femaleRB = view.findViewById(R.id.female);
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                genderSelected = i == R.id.male;
            }
        });
        userNewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/" + GlobalConfig.SIVICO_DIR);
                folder.mkdirs();
                File photo = new File(folder.toString(), Utils.getPhotoName());
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photo));
                imageUri = Uri.fromFile(photo);
                NewUserActivityFragment.this.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        userNewBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                Date date = c.getTime();
                                userBirthday.setText(Utils.getFormatDate(date.getTime() / 1000));
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindFromControls(callback);
            }
        });
    }

    private void bindFromControls(OnEditUser callback) {
        Date date = new Date();
        try {
            date = Utils.sivicoDateFormat.parse(userBirthday.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, e.toString(), e.fillInStackTrace());
        }
        if (editedUser != null) {
            editedUser.setName(userName.getText().toString());
            editedUser.setIdNumber(userIdNumber.getText().toString());
            editedUser.setGender(genderSelected);
            editedUser.setBirthday(String.valueOf(date.getTime() / 1000));
            editedUser.setPhone(userPhone.getText().toString());
            editedUser.setRegion(region.getText().toString());
            editedUser.setNeighborhood(neighborhood.getText().toString());
            if (imageUri != null) {
                uploadPhoto(callback, editedUser);
                return;
            }
            callback.onEditUser(editedUser);
        }
        isAnonymous = anonymousSwitch.isChecked();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Uri photoUrl = firebaseUser.getPhotoUrl();
        String uri;
        if (photoUrl == null) {
            uri = "no-pic";
        } else {
            uri = photoUrl.toString();
        }
        User newUser = new User(
                firebaseUser.getUid(),
                firebaseUser.getDisplayName(),//checks if name change
                userIdNumber.getText().toString(),
                genderSelected,
                String.valueOf(date.getTime() / 1000),
                userPhone.getText().toString(),
                region.getText().toString(),
                neighborhood.getText().toString(),
                firebaseUser.getEmail(),
                uri,
                0
        );
        newUser.setAnonymous(isAnonymous);
        if (newUser.getPhoto().equals("no-pic")) {
            if (imageUri != null) {
                uploadPhoto(callback, newUser);
                return;
            }
            Toast.makeText(getContext(), getString(R.string.user_no_image), Toast.LENGTH_LONG).show();
        }
        callback.onEditUser(newUser);

    }

    private void uploadPhoto(final OnEditUser callback, final User newUser) {
        String uuid = UUID.randomUUID().toString();
        mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(imageUri)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String path = taskSnapshot.getMetadata().getReference().getPath();
                        Log.d(TAG, "uploadPhoto:onSuccess:" + path);
                        Toast.makeText(getContext(), "Image uploaded",
                                Toast.LENGTH_SHORT).show();

                        newUser.setPhoto(taskSnapshot.getDownloadUrl().toString());
                        callback.onEditUser(newUser);
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

    private void bindUser(FirebaseUser user) {
        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null) {
            Utils.loadRoundPhoto(getContext(), userPhoto, getResources(), photoUrl.toString());
        }
        userName.setText(user.getDisplayName());
        userPhone.setText(user.getPhoneNumber());
    }

    private void bindUser(User user) {
        Utils.loadRoundPhoto(getContext(), userPhoto, getResources(), user.getPhoto());
        userName.setText(user.getName());
        userBirthday.setText(Utils.getFormatDate(user.getBirthday()));
        RadioButton rb = femaleRB;
        if (user.isGender()) {
            rb = maleRB;
        }
        rb.setSelected(true);
        userIdNumber.setText(user.getIdNumber());
        userPhone.setText(user.getPhone());
        region.setText(user.getRegion());
        neighborhood.setText(user.getNeighborhood());
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
        Utils.loadRoundPhoto(getContext(), userPhoto, getResources(), photo);
        userPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OnEditUser) activity;
    }
}
