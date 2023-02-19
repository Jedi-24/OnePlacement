package com.jedi.oneplacement.admin.utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.navigation.NavDeepLinkBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jedi.oneplacement.R;
import com.jedi.oneplacement.activities.EntryActivity;
import com.jedi.oneplacement.activities.MainActivity;
import com.jedi.oneplacement.utils.AppConstants;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    private static final String CHANNEL_ID = "CHANNEL_69";
    int count = 0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
//        RemoteMessage.Notification notification = message.getNotification();
        Map<String, String> notifPayload = message.getData();

//        if (notification == null) {
//            Log.d(TAG, "onMessageReceived: NOTIF NULL");
//            return;
//        }
        if (notifPayload.isEmpty()) {
            Log.d(TAG, "onMessageReceived: NOTIF NULL");
            return;
        }

        String title = notifPayload.get(AppConstants.TITLE);
        String msg = notifPayload.get(AppConstants.BODY);

        SharedPreferences sharedPreferences = this.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String devT = sharedPreferences.getString(AppConstants.DEV_TOKEN, null);

        // todo: to not send notification to logged out users:
        Log.d(TAG, "onMessageReceived:check token " + devT);

        if (devT == null || (devT != null && devT.matches(AppConstants.JEDI)))
            return;
        getFirebaseMessage(title, msg);
    }

    public void getFirebaseMessage(String title, String msg) {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, AppConstants.APP_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), EntryActivity.class);
        intent.putExtra(AppConstants.APP, title);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity(getApplicationContext(), 69, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

//        PendingIntent pendingIntent = new NavDeepLinkBuilder(getApplicationContext())
//                .setComponentName(EntryActivity.class)
//                .setGraph(R.navigation.main_nav_graph)
//                .setDestination(R.id.companyFragment)
//                .setArguments(new Bundle())
//                .createPendingIntent();

//        TaskStackBuilder tsb = TaskStackBuilder.create(this);
//        tsb.addNextIntentWithParentStack(new NavDeepLinkBuilder(getApplicationContext())
//                .setComponentName(MainActivity.class)
//                .setGraph(R.navigation.main_nav_graph)
//                .setDestination(R.id.companyFragment)
//                .setArguments(new Bundle())
//                .createTaskStackBuilder()

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.tie)
                .setVibrate(new long[]{1000})
                .setSound(notifSound)
                .setContentTitle(title)
                .setContentIntent(resultIntent)
                .setContentText(msg);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        manager.notify(count, notification);
        count++;
    }
}