package com.rocket.sivico.GUI;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;

import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.R;

public class UserActivity extends SivicoMenuActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        loadActionBar();
        manageToolbar();
    }

    private void manageToolbar() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_user));
    }
}
