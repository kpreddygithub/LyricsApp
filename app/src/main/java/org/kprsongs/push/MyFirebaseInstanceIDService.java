package org.kprsongs.push;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.kprsongs.CommonConstants;
import org.kprsongs.utils.SharedPrefUtils;

/**
 * Created by MyDukan on 22-02-2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    String TAG = "MyFirebaseInstanceIDService";
    Activity activity;

    public MyFirebaseInstanceIDService() {
    }

    public MyFirebaseInstanceIDService(Activity activity) {
        super();
        this.activity = activity;
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPrefUtils.putData(this, CommonConstants.FcmTOKEN, refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String refreshToken) {
        SharedPrefUtils.putData(this, CommonConstants.isRefreshedToken, true);
    }

}
