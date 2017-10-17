package com.rocket.sivico.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.Interfaces.OnEditUser;
import com.rocket.sivico.R;

import java.util.Map;

public class NewUserActivity extends AppCompatActivity implements OnEditUser {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        database.getRef().child(user.getId()).push();

        Map<String, Object> stringObjectMap = user.toMap();
        database.getRef().child(user.getId()).updateChildren(stringObjectMap);

        finish();
    }
}
