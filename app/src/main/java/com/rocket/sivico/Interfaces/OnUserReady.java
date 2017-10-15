package com.rocket.sivico.Interfaces;

import com.google.firebase.auth.FirebaseUser;
import com.rocket.sivico.Data.User;

/**
 * Created by JuanCamilo on 11/10/2017.
 */

public interface OnUserReady {
    void onUserReady(User user);

    void onNewUser(FirebaseUser user);
}
