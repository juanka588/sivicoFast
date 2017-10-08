package com.rocket.sivico.GUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class UserActivity extends SivicoMenuActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        loadActionBar();
        manageToolbar();
        bindUserData();
    }

    private void bindUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        User user = GlobalConfig.getUser(currentUser);
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        TextView phone = findViewById(R.id.user_phone);
        phone.setText(mPhoneNumber);

        TextView name = findViewById(R.id.user_name);
        name.setText(user.getName());

        TextView idNumber = findViewById(R.id.user_id_number);
        idNumber.setText(user.getIdNumber());

        TextView gender = findViewById(R.id.user_gender);
        gender.setText(user.isGender() ? "Masculino" : "Femenino");

        TextView age = findViewById(R.id.user_age);
        age.setText(user.getAge() + "");

        TextView region = findViewById(R.id.user_region);
        region.setText(user.getRegion());

        TextView email = findViewById(R.id.user_email);
        email.setText(user.getEmail());

        TextView neigh = findViewById(R.id.user_phone);
        neigh.setText(user.getNeighborhood());

        final ImageView image = findViewById(R.id.user_photo);
        Picasso.with(this)
                .load(currentUser.getPhotoUrl())
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap source = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        RoundedBitmapDrawable drawable =
                                RoundedBitmapDrawableFactory.create(UserActivity.this
                                        .getResources(), source);
                        drawable.setCircular(true);
                        drawable.setCornerRadius(Math.max(source.getWidth() / 2.0f, source.getHeight() / 2.0f));
                        image.setImageDrawable(drawable);
                    }

                    @Override
                    public void onError() {

                    }
                });

        ProgressBar progressBar = findViewById(R.id.user_score_progress);
        progressBar.setProgress(30);

    }

    private void manageToolbar() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_user));
    }
}
