package com.rocket.sivico.Data;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rocket.sivico.Interfaces.OnUserReady;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JuanCamilo on 2/10/2017.
 */

public class GlobalConfig {
    public static final String PARAM_REPORT = "report";
    public static final Map<String[], String[]> temp = new HashMap<>();
    public static final String PARAM_CATEGORY = "category";
    private static final String TAG = GlobalConfig.class.getSimpleName();
    public static DatabaseReference userRef;


    public static void getUser(FirebaseUser user, OnUserReady callback) {
        userRef = FirebaseDatabase.getInstance().getReference("users").getRef().child(user.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, dataSnapshot.child("name").getValue() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        User userRet = new User(user.getUid()
                , user.getDisplayName()
                , "1013642638"
                , true
                , 24
                , user.getPhoneNumber()
                , "Bogota"
                , "Restrepo"
                , user.getEmail(), user.getPhotoUrl().toString() + "", 78);
        callback.onUserReady(userRet);
    }
}