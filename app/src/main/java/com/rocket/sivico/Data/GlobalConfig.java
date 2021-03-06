package com.rocket.sivico.Data;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rocket.sivico.Interfaces.OnUserReady;

/**
 * Created by JuanCamilo on 2/10/2017.
 */

public class GlobalConfig {
    public static final String PARAM_REPORT = "report";
    public static final String PARAM_CATEGORY = "category";
    public static final String PARAM_SUBCATEGORY = "subcategory";
    private static final String TAG = GlobalConfig.class.getSimpleName();
    public static final String SIVICO_DIR = "sivico";
    public static final String PARAM_USER = "user";
    public static final String DEFAULT_LATITUDE = "4.6482837";
    public static final String DEFAULT_LONGITUDE = "-74.2478934";
    public static DatabaseReference userRef;

    static {
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e.fillInStackTrace());
        }

    }

    public static void getUser(final FirebaseUser user, final OnUserReady callback) {
        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
        userRef.keepSynced(true);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.e(TAG, dataSnapshot.child("name").getValue() + "");
                    User userRet = new User(user.getUid()
                            , dataSnapshot.child("name").getValue().toString()
                            , dataSnapshot.child("id_number").getValue().toString()
                            , ((Boolean) (dataSnapshot.child("gender").getValue())).booleanValue()
                            , dataSnapshot.child("birthday").getValue().toString()
                            , dataSnapshot.child("phone").getValue().toString()
                            , dataSnapshot.child("region").getValue().toString()
                            , dataSnapshot.child("neighborhood").getValue().toString()
                            , dataSnapshot.child("email").getValue().toString()
                            , dataSnapshot.child("photo").getValue().toString()
                            , ((Long) dataSnapshot.child("points").getValue()).intValue());
                    callback.onUserReady(userRet);
                } else {
                    callback.onNewUser(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}