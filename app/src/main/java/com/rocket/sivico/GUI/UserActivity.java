package com.rocket.sivico.GUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.Data.User;
import com.rocket.sivico.Interfaces.OnUserReady;
import com.rocket.sivico.R;
import com.rocket.sivico.Utils;

public class UserActivity extends SivicoMenuActivity implements OnUserReady {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        loadActionBar();
        manageToolbar();
        FloatingActionButton fab = findViewById(R.id.edit_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserActivity.this, NewUserActivity.class);
                intent.putExtra(GlobalConfig.PARAM_USER, currentUser);
                startActivity(intent);
            }
        });
    }

    private void bindUserData(User user) {
        TextView phone = findViewById(R.id.user_phone);
        phone.setText(user.getPhone());

        TextView name = findViewById(R.id.user_name);
        name.setText(user.getName());

        TextView idNumber = findViewById(R.id.user_id_number);
        idNumber.setText(user.getIdNumber());

        TextView gender = findViewById(R.id.user_gender);
        gender.setText(user.isGender() ? getString(R.string.male) : getString(R.string.female));

        TextView age = findViewById(R.id.user_age);
        age.setText(Utils.getAge(user.getBirthday()));

        TextView region = findViewById(R.id.user_region);
        region.setText(user.getRegion());

        TextView email = findViewById(R.id.user_email);
        email.setText(user.getEmail());

        TextView neigh = findViewById(R.id.user_neighborhood);
        neigh.setText(user.getNeighborhood());

        final ImageView image = findViewById(R.id.user_photo);
        Utils.loadRoundPhoto(getApplicationContext(), image, getResources(), user.getPhoto());
        ProgressBar progressBar = findViewById(R.id.user_score_progress);
        progressBar.setProgress(user.getScore());

        TextView scoreText = findViewById(R.id.user_score_text);
        scoreText.setText(getString(R.string.points) + user.getScore());
    }

    @Override
    public void onUserReady(User user) {
        super.onUserReady(user);
        bindUserData(user);
    }

    private void manageToolbar() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_user));
    }
}
