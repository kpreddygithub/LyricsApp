package org.kprsongs.push;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.kprsongs.activity.NavigatinDrawerActivity;
import org.kprsongs.glorytogod.R;

public class MyFirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            if (remoteMessage != null) {
                if (remoteMessage.getNotification() != null) {
                    if (remoteMessage.getNotification().getBody() != null) {
                        String message = remoteMessage.getNotification().getBody();
                        String title = remoteMessage.getNotification().getTitle();
                        setNotification(title, message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setNotification(String title, String message) {

        Intent intent = new Intent(this, NavigatinDrawerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary_1000));
        builder.setContentTitle(title);
        builder.setContentText(message);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
    }

}
