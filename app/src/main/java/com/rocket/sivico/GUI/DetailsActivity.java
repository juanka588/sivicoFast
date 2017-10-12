package com.rocket.sivico.GUI;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.R;

public class DetailsActivity extends SivicoMenuActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        loadActionBar();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        fragment.setArguments(getIntent().getExtras());
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }


}
