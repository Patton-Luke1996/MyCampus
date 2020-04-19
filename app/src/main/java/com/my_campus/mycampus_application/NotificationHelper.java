package com.my_campus.mycampus_application;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channel1ID = "channel1ID";
    public static final String channel1Name= "Renew Item";
    private NotificationManager mManager;


    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannel(){

        NotificationChannel renewChannel= new NotificationChannel(channel1ID,
                channel1Name, NotificationManager.IMPORTANCE_DEFAULT);

        renewChannel.enableLights(true);
        renewChannel.enableVibration(true);
        renewChannel.setLightColor(R.color.colorPrimary);
        renewChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(renewChannel);
    }

    public NotificationManager getManager()
    {
        if (mManager==null)
        {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return  mManager;
    }

    public NotificationCompat.Builder getChannel1Notification()
    {
        Intent myIntent = new Intent(this, AppMainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channel1ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Renew Listing")
                .setContentText("Please renew your listing or else it will be automatically deleted in 3 days.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


    }


}

