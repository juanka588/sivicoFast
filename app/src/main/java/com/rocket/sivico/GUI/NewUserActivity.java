package com.rocket.sivico.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.Interfaces.OnEditUser;
import com.rocket.sivico.R;

import java.util.Map;

public class NewUserActivity extends SivicoMenuActivity implements OnEditUser {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        loadActionBar();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NewUserActivityFragment fragment = new NewUserActivityFragment();
        fragment.setArguments(getIntent().getExtras());
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onEditUser(User user) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        Map<String, Object> stringObjectMap = user.toMap();
        stringObjectMap.put("lat", userPos.latitude);
        stringObjectMap.put("lon", userPos.longitude);
        database.getRef().child(user.getId()).updateChildren(stringObjectMap);
        finish();
    }
}
