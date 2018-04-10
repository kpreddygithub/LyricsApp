package org.kprsongs.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
        }*/ else if (id == R.id.nav_share) {
            shareTheLink();
        } else if (id == R.id.nav_whatsapp) {
            onWhatsAppBtnClick();
        }


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

    private void shareTheLink() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.share_msg_subject));
        String url = "https://play.google.com/store/apps/details?id=org.app.mydukan";
        share.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_msg_text) + "\n " + url);
        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void onWhatsAppBtnClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (contactExists(this, "+918074027107")) {
                    sendwhatsapp();
                }
                if (!contactExists(this, "+918074027107")) {
                    addcontact();
                }
            } else {
                showPermissions();
            }
        } else {
            if (contactExists(this, "+918074027107")) {
                sendwhatsapp();
            }
            if (!contactExists(this, "+918074027107")) {
                addcontact();
            }
        }
    }

    public void sendwhatsapp() {
        Uri uri = Uri.parse("smsto:" + "+918074027107");
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }

    public boolean contactExists(Context context, String number) {
        // number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
        try {
            if (cur != null) {
                if (cur.moveToFirst()) {
                    return true;
                }
            }
        } finally {
            if (cur != null)
                cur.close();
        }

        return false;
    }

    public void addcontact() {
        try {
            Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
            contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

            contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, "TeluguLyricsApp")
                    .putExtra(ContactsContract.Intents.Insert.PHONE, "+918074027107");
            startActivityForResult(contactIntent, 1);

        } catch (Exception e) {
            Log.i("Cannot add", " cannot add");
        }
    }

    private void showPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.e("testing", "Permission is granted");
            } else {
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Info")
                        .setMessage("Please do not deny any permissions, accept all permissions")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue
                                ActivityCompat.requestPermissions(NavigatinDrawerActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE}, 1);
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.e("testing", "Permission is already granted");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i("permission", "granted");
        } else {
            Log.i("permission", "revoked");
        }
    }


}
