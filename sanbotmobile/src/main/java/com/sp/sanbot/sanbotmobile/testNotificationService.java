package com.sp.sanbot.sanbotmobile;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by vootl on 24/1/2018.
 */

public class testNotificationService extends Service {

    DatabaseReference databaseReference;

    String valueNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("NotificationHehe");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                valueNotification = dataSnapshot.getValue().toString();
                switch (valueNotification){
                    case "1" : sendNotification();
                        Log.i("notificationvalue", "Number Received: " + valueNotification);
                        databaseReference.setValue(0);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return START_STICKY;
    }


    public void sendNotification(){
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.common_google_signin_btn_icon_dark))
                .setContentTitle("ALERT")
                .setContentText("MOTION DETECTED");

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
