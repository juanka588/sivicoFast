package com.rocket.sivico.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.R;

public class NewReportActivity extends SivicoMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        loadActionBar();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NewReportActivityFragment fragment = new NewReportActivityFragment();
        fragment.setArguments(getIntent().getExtras());
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resultIntent = new Intent(this, CategoryActivity.class);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
