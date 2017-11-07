package com.rocket.sivico.GUI;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.rocket.sivico.Data.GlobalConfig;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.Data.SivicoMenuActivity;
import com.rocket.sivico.R;

public class DetailsActivity extends SivicoMenuActivity {

    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        loadActionBar();
        report = getIntent().getExtras().getParcelable(GlobalConfig.PARAM_REPORT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DetailsActivityFragment fragment = new DetailsActivityFragment();
        fragment.setArguments(getIntent().getExtras());
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_share:
//                ShareLinkContent content = new ShareLinkContent.Builder()
//                        .setContentUrl(Uri.parse(report.getEvidence().get("img1").toString()))
//                        .setQuote(report.getDescription())
//                        .setShareHashtag(new ShareHashtag.Builder()
//                                .setHashtag("#sivicoReports")
//                                .build())
//                        .build();
//                ShareDialog shareDialog = new ShareDialog(DetailsActivity.this);
//                shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                break;
        }
        return true;
    }
}
