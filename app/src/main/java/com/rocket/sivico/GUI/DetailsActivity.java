package com.rocket.sivico.GUI;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.R;

public class DetailsActivity extends SivicoMenuActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        loadActionBar();
        manageToolbar();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        fragment.setArguments(getIntent().getExtras());
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();

    }

    private void manageToolbar() {
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(getString(R.string.title_activity_category));
    }

}
