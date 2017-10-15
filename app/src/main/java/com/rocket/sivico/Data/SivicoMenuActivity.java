package com.rocket.sivico.Data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rocket.sivico.GUI.CategoryActivity;
import com.rocket.sivico.GUI.MainActivity;
import com.rocket.sivico.GUI.NewUserActivity;
import com.rocket.sivico.GUI.ReportsActivity;
import com.rocket.sivico.GUI.UserActivity;
import com.rocket.sivico.Interfaces.OnUserReady;
import com.rocket.sivico.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by JuanCamilo on 16/10/2016.
 */

public class SivicoMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , OnUserReady {

    private static final String TAG = SivicoMenuActivity.class.getSimpleName();
    protected NavigationView navigationView;
    protected Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private ImageView image;
    private TextView name;
    private TextView email;
    protected FirebaseUser firebaseUser;

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

    @Override
    public void onUserReady(User user) {
        name.setText(user.getName());
        email.setText(user.getEmail());

        Picasso.with(this)
                .load(user.getPhoto())
                .into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap source = ((BitmapDrawable) image.getDrawable()).getBitmap();
                        RoundedBitmapDrawable drawable =
                                RoundedBitmapDrawableFactory.create(SivicoMenuActivity.this
                                        .getResources(), source);
                        drawable.setCircular(true);
                        drawable.setCornerRadius(Math.max(source.getWidth() / 2.0f, source.getHeight() / 2.0f));
                        image.setImageDrawable(drawable);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    @Override
    public void onNewUser(FirebaseUser user) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    protected void loadActionBar() {
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        image = navigationView.getHeaderView(0).findViewById(R.id.user_image_nav);
        name = navigationView.getHeaderView(0).findViewById(R.id.user_name_nav);
        email = navigationView.getHeaderView(0).findViewById(R.id.user_email_nav);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        GlobalConfig.getUser(firebaseUser, this);
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
            case R.id.nav_sign_out:
                signOut();
                return true;
            default:
                intent = new Intent(this, ReportsActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(SivicoMenuActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Log.e(TAG, "signOut failed");
                        }
                    }
                });
    }
}
