package org.kprsongs;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by K Purushotham Reddy on 10/19/2014.
 */
public class SongsApplication extends MultiDexApplication {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
