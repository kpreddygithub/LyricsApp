package org.kprsongs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.kprsongs.CommonConstants;
import org.kprsongs.domain.User;
import org.kprsongs.fragment.HomeTabFragment;
import org.kprsongs.glorytogod.R;
import org.kprsongs.utils.SharedPrefUtils;

public class NavigatinDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference database;
    private boolean isRefreshedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigatin_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        String accountName = SharedPrefUtils.getString(this, CommonConstants.AccountName, "");
        String email = SharedPrefUtils.getString(this, CommonConstants.AccountEmail, "");
        if (!accountName.isEmpty())
            ((TextView) header.findViewById(R.id.user_name)).setText(accountName);
        if (!email.isEmpty())
            ((TextView) header.findViewById(R.id.user_email)).setText(email);

        ((ImageView) header.findViewById(R.id.user_image)).setImageResource(R.drawable.user);

        fragmentTransaction(new HomeTabFragment(), R.id.main_framelayout);
        isRefreshedToken = SharedPrefUtils.getBoolean(this, CommonConstants.isRefreshedToken, false);
        if (isRefreshedToken) sendRegistrationToServer();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigatin_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*
        if (id == R.id.action_settings) {
            return true;
        }
*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentTransaction(new HomeTabFragment(), R.id.main_framelayout);
        } /*else if (id == R.id.nav_settings) {
            Intent intent = new Intent(NavigatinDrawerActivity.this, UserSettingActivity.class);
            startActivity(intent);
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fragmentTransaction(Fragment newFragment, int container) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(container, newFragment);
        transaction.commit();
    }

    public void sendRegistrationToServer() {
        database = FirebaseDatabase.getInstance().getReference();
        User user = new User();
        String email = SharedPrefUtils.getString(this, CommonConstants.AccountEmail, "");
        String name = SharedPrefUtils.getString(this, CommonConstants.AccountName, "");
        String id = SharedPrefUtils.getString(this, CommonConstants.AccountToken, "");
        user.setName(name);
        user.setEmail(email);
        user.setGoogleId(id);
        user.setFcmToken(SharedPrefUtils.getString(this, CommonConstants.FcmTOKEN, ""));
        database.child("users").child(id).setValue(user);
        SharedPrefUtils.putData(this, CommonConstants.isRefreshedToken, false);
    }
}
