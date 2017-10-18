package com.rocket.sivico.GUI;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.Interfaces.OnEditUser;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

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
    private Spinner gender;
    private EditText region;
    private EditText neighborhood;
    private OnEditUser callback;
    private FloatingActionButton saveUser;
    private Uri imageUri;
    private User editedUser;
    private int mYear;
    private int mMonth;
    private int mDay;

    public NewUserActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);
        bindControls(view);
        try {
            Bundle bundle = getArguments();
            editedUser = bundle.getParcelable(GlobalConfig.PARAM_USER);
            if (editedUser != null) {
                bindUser(editedUser);
            }
        } catch (Exception e) {
            editedUser = null;
            bindUser(FirebaseAuth.getInstance().getCurrentUser());
        }
        return view;
    }

    private void bindControls(View view) {
        userPhoto = view.findViewById(R.id.user_photo);
        userNewPic = view.findViewById(R.id.take_user_photo);
        userName = view.findViewById(R.id.user_name);
        userBirthday = view.findViewById(R.id.user_birthday);
        userNewBirthDay = view.findViewById(R.id.select_date);
        gender = view.findViewById(R.id.user_gender_select);
        userIdNumber = view.findViewById(R.id.user_id_number);
        userPhone = view.findViewById(R.id.user_phone);
        region = view.findViewById(R.id.user_region);
        neighborhood = view.findViewById(R.id.user_neighborhood);
        saveUser = view.findViewById(R.id.fab);

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
                datePickerDialog.show();
            }
        });
        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = bindFromControls();
                callback.onEditUser(user);
            }
        });
    }

    private User bindFromControls() {
        Date date = new Date();
        try {
            date = Utils.sivicoDateFormat.parse(userBirthday.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (editedUser != null) {
            editedUser.setName(userName.getText().toString());
            editedUser.setIdNumber(userIdNumber.getText().toString());
            editedUser.setGender(gender.getSelectedItemPosition() == 0);
            editedUser.setBirthday(String.valueOf(date.getTime() / 1000));
            editedUser.setPhone(userPhone.getText().toString());
            editedUser.setRegion(region.getText().toString());
            editedUser.setNeighborhood(neighborhood.getText().toString());
            return editedUser;
        }
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User newUser = new User(
                firebaseUser.getUid(),
                firebaseUser.getDisplayName(),//checks if name change
                userIdNumber.getText().toString(),
                gender.getSelectedItemPosition() == 0,
                String.valueOf(date.getTime() / 1000),
                userPhone.getText().toString(),
                region.getText().toString(),
                neighborhood.getText().toString(),
                firebaseUser.getEmail(),
                firebaseUser.getPhotoUrl().toString(),//TODO: check if new photo
                0
        );
        return newUser;
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
        gender.setSelection(user.isGender() ? 0 : 1);
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
