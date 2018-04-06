package org.kprsongs.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.kprsongs.CommonConstants;
import org.kprsongs.SongsApplication;
import org.kprsongs.dao.SongDao;
import org.kprsongs.domain.User;
import org.kprsongs.glorytogod.R;
import org.kprsongs.task.AsyncGitHubRepositoryTask;
import org.kprsongs.utils.LogUtils;
import org.kprsongs.utils.PropertyUtils;
import org.kprsongs.utils.SharedPrefUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * @Author : K Purushotham Reddy
 * @Version : 1.0
 */
public class SplashScreenActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String GET_REQUEST = "GET";
    public static final String DATABASE_UPDATED_DATE_KEY = "databaseUpdatedDateKey";
    public static final String DATE_PATTERN = "dd/MM/yyyy";
    private SongDao songDao;
    private SharedPreferences sharedPreferences;
    private AsyncGitHubRepositoryTask asyncGitHubRepositoryTask;
    private ProgressBar progressBar;
    private File commonPropertyFile;
    private File externalCacheDir;

    private static final int GOOGLE_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount googleSignInAccount;
    private String displayName;
    private DatabaseReference database;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getSupportActionBar().hide();

        if (shouldAskPermissions()) {
            verifyStoragePermissions(this);
        }

        asyncGitHubRepositoryTask = new AsyncGitHubRepositoryTask(this);
        setContentView(R.layout.splash_screen);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        songDao = new SongDao(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SongsApplication.getContext());
        commonPropertyFile = PropertyUtils.getPropertyFile(this, CommonConstants.COMMON_PROPERTY_TEMP_FILENAME);
        externalCacheDir = getExternalCacheDir();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        loadDatabase();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.btn_RegisterMob).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInToGoogle();
            }
        });

    }

    private void loadDatabase() {
        try {
            SplashScreenActivity context = SplashScreenActivity.this;
            String projectVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            Log.i(SplashScreenActivity.class.getSimpleName(), "Project version " + projectVersion);
            if (projectVersion.contains("SNAPSHOT") && isWifi()) {
                if (asyncGitHubRepositoryTask.execute().get()) {
                    Log.i(SplashScreenActivity.class.getSimpleName(), "Preparing to copy remote database....");
                    new AsyncDownloadTask(context, projectVersion).execute();
                } else {
                    moveToMainActivity();
                }
            } else {
                copyBundleDatabase(context, projectVersion);
            }
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Error occurred while loading database");
        }
    }


    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    public final boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.isConnected()) {
                if ((networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
                    return true;
                }
            }
        }
        Log.i(UserSettingActivity.class.getSimpleName(), "System does not connect with wifi");
        return false;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public class AsyncDownloadTask extends AsyncTask<String, Void, Boolean> {
        private String projectVersion;

        public AsyncDownloadTask() {

        }

        public AsyncDownloadTask(Context context, String projectVersion) {
            this.projectVersion = projectVersion;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            File destinationFile = null;
            try {
//                String remoteUrl = "https://raw.githubusercontent.com/crunchersaspire/worshipsongs-db-dev/master/songs.sqlite";
                String remoteUrl = "https://raw.githubusercontent.com/kpreddygithub/lyrics/master/songs.sqlite";
                String className = SplashScreenActivity.this.getClass().getSimpleName();
                destinationFile = File.createTempFile("download-songs", "sqlite", externalCacheDir);
                URL url = new URL(remoteUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(GET_REQUEST);
                urlConnection.setConnectTimeout(60000);
                urlConnection.setReadTimeout(60000);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                DataInputStream dataInputStream = new DataInputStream(urlConnection.getInputStream());
                int contentLength = urlConnection.getContentLength();
                Log.d(className, "Content length " + contentLength);
                byte[] buffer = new byte[contentLength];
                dataInputStream.readFully(buffer);
                dataInputStream.close();
                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(destinationFile));
                dataOutputStream.write(buffer);
                dataOutputStream.flush();
                dataOutputStream.close();
                Log.i(className, "Finished downloading file!");
                songDao.copyDatabase(destinationFile.getAbsolutePath(), true);
                songDao.open();
                PropertyUtils.setProperty(DATABASE_UPDATED_DATE_KEY, DateFormatUtils.format(new Date(), DATE_PATTERN), commonPropertyFile);
                return true;
            } catch (Exception ex) {
                Log.e(this.getClass().getSimpleName(), "Error", ex);
                return false;
            } finally {
                destinationFile.deleteOnExit();
            }
        }

        @Override
        protected void onPostExecute(Boolean successfull) {
            if (successfull) {
                Log.i(SplashScreenActivity.class.getSimpleName(), "Remote development database copied successfully.");
                moveToMainActivity();
            } else {
                copyBundleDatabase(SplashScreenActivity.this, projectVersion);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void copyBundleDatabase(SplashScreenActivity context, String projectVersion) {
        try {
            File commonPropertyFile = PropertyUtils.getPropertyFile(context, CommonConstants.COMMON_PROPERTY_TEMP_FILENAME);
            String existingVersion = PropertyUtils.getProperty(CommonConstants.VERSION_KEY, commonPropertyFile);
            Log.i(SplashScreenActivity.class.getSimpleName(), "Project version in property file" + existingVersion);
            if (StringUtils.isNotBlank(existingVersion) && existingVersion.equalsIgnoreCase(projectVersion)) {
                Log.i(SplashScreenActivity.class.getSimpleName(), "Bundle database already copied.");
            } else {
                Log.i(SplashScreenActivity.class.getSimpleName(), "Preparing to copy bundle database.");
                songDao.copyDatabase("", true);
                songDao.open();
                PropertyUtils.setProperty(CommonConstants.VERSION_KEY, projectVersion, commonPropertyFile);
                Log.i(SplashScreenActivity.class.getSimpleName(), "Bundle database copied successfully.");
            }
//            moveToMainActivity();
        } catch (Exception ex) {
            Log.e(this.getClass().getSimpleName(), "Error occurred while coping databases", ex);
        }
    }

    void moveToMainActivity() {
        Intent intent = new Intent(SplashScreenActivity.this, NavigatinDrawerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.splash_fade_in, R.anim.splash_fade_out);
        SplashScreenActivity.this.finish();
    }

    public void signInToGoogle() {
        googleLogout();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    public void googleLogout() {
        if (mGoogleApiClient.isConnected()) {
//            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.i("Google Logout:", "Logged out successfully");
                }
            });

        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            if (data != null) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    setGoogleSignInAccount(account);
                } else {
                    LogUtils.i("Google Sign In failed");
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            LogUtils.i("Google Sign In failed");
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setGoogleSignInAccount(GoogleSignInAccount account) {
        String[] parts = account.getDisplayName().split("\\s+");
        if (parts.length == 2) {
            String fName = parts[0];
            String lName = parts[1];
            displayName = fName;
        } else {
            displayName = account.getDisplayName();
        }
        String photoUrl = String.valueOf(account.getPhotoUrl());
        googleSignInAccount = account;

        SharedPrefUtils.putData(this, CommonConstants.AccountName, displayName);
        SharedPrefUtils.putData(this, CommonConstants.AccountEmail, googleSignInAccount.getEmail());
        SharedPrefUtils.putData(this, CommonConstants.AccountToken, googleSignInAccount.getId());
        SharedPrefUtils.putData(this, CommonConstants.AccountPhoto, photoUrl);
        database = FirebaseDatabase.getInstance().getReference();
        User user = new User();
        user.setEmail(googleSignInAccount.getEmail());
        user.setName(displayName);
        user.setGoogleId(googleSignInAccount.getId());
        user.setFcmToken(SharedPrefUtils.getString(this, CommonConstants.FcmTOKEN, ""));
        database.child("users").child(googleSignInAccount.getId()).setValue(user);
        moveToMainActivity();
    }
}