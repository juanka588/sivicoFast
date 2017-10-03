package com.rocket.sivico.Data;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocket.sivico.GUI.CategoryActivity;
import com.rocket.sivico.GUI.MainActivity;
import com.rocket.sivico.GUI.ReportsActivity;
import com.rocket.sivico.GUI.UserActivity;
import com.rocket.sivico.R;

/**
 * Created by JuanCamilo on 16/10/2016.
 */

public class SivicoMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = SivicoMenuActivity.class.getSimpleName();
    protected NavigationView navigationView;
    protected Toolbar mToolbar;
    private DrawerLayout drawerLayout;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.e(TAG, "setting call");
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reports, menu);
        return true;
    }

    protected void loadActionBar() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView image = navigationView.getHeaderView(0).findViewById(R.id.circle_image);
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.user_name);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.user_email);
        //TODO: bind data
        navigationView.getMenu().clear(); //clear old inflated items.
        navigationView.inflateMenu(R.menu.activity_reports_drawer); //inflate new items.

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.nav_new_report:
                intent = new Intent(this, CategoryActivity.class);
                break;
            case R.id.nav_my_reports:
                intent = new Intent(this, ReportsActivity.class);
                break;
            case R.id.nav_manage:
                intent = new Intent(this, UserActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
