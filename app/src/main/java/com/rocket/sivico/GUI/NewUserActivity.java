package com.rocket.sivico.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.LocalizedActivity;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.Interfaces.OnEditUser;
import com.rocket.sivico.R;

import java.util.Map;

public class NewUserActivity extends LocalizedActivity implements OnEditUser {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initLocalization();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NewUserActivityFragment fragment = new NewUserActivityFragment();
        fragment.setArguments(getIntent().getExtras());
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onEditUser(User user) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        Map<String, Object> stringObjectMap = user.toMap();
        if (userPos != null) {
            stringObjectMap.put("lat", userPos.latitude);
            stringObjectMap.put("lon", userPos.longitude);
        } else {
            stringObjectMap.put("lat", GlobalConfig.DEFAULT_LATITUDE);
            stringObjectMap.put("lon", GlobalConfig.DEFAULT_LONGITUDE);
        }
        database.getRef().child(user.getId()).updateChildren(stringObjectMap);
        Intent intent = new Intent(this, ReportsActivity.class);
        startActivity(intent);
        this.finish();
    }
}
