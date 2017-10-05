package com.rocket.sivico.GUI;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.R;

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
        User user = GlobalConfig.getUser();
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        TextView phone = findViewById(R.id.user_phone);
        phone.setText(mPhoneNumber);

        TextView name = findViewById(R.id.user_name);
        name.setText(user.getName());

        TextView lastName = findViewById(R.id.user_last_name);
        lastName.setText(user.getLastName());

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

        TextView neigh = findViewById(R.id.user_neighborhood);
        neigh.setText(user.getNeighborhood());

    }

    private void manageToolbar() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_user));
    }
}
