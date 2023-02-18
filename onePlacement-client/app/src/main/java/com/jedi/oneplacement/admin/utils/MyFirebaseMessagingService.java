package com.jedi.oneplacement.admin.utils;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.utils.AppConstants;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    private static final String CHANNEL_ID = "CHANNEL_69";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        RemoteMessage.Notification notification = message.getNotification();
        if (notification == null) {
            Log.d(TAG, "onMessageReceived: NOTIF NULL");
            return;
        }

        String title = notification.getTitle();
        String msg = notification.getBody();
        getFirebaseMessage(title, msg);
    }
    public void getFirebaseMessage(String title, String msg) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, AppConstants.APP_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        manager.notify(0, builder.build());
    }
}