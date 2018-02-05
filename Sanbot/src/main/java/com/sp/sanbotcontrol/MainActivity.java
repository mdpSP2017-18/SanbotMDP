package com.sp.sanbotcontrol;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qihancloud.opensdk.base.TopBaseActivity;
import com.sp.FaceTracking.FaceTrackerActivity;

public class MainActivity extends TopBaseActivity implements View.OnClickListener{

    Button logoutButton;
    Button controlButton;
    Button vidButton;
    Button mapButton;
    Button mapButton2;
    Button testButton;
    Button testNotificaiton;

    FirebaseAuth firebaseAuth;
    DatabaseReference triggerVideoButton;
    String triggerValue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        logoutButton = (Button)findViewById(R.id.logoutButton);
        controlButton = (Button)findViewById(R.id.controlButon);
        vidButton = (Button)findViewById(R.id.vidButton);
        mapButton = (Button)findViewById(R.id.mapButton);
        mapButton2 = (Button)findViewById(R.id.mapButton2);
        testButton = (Button)findViewById(R.id.testFaceTrack);
        testNotificaiton = (Button)findViewById(R.id.testNotification);


        firebaseAuth = FirebaseAuth.getInstance();
        triggerVideoButton = FirebaseDatabase.getInstance().getReference("triggerVideo");

        logoutButton.setOnClickListener(this);
        controlButton.setOnClickListener(this);
        vidButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
        mapButton2.setOnClickListener(this);
        testButton.setOnClickListener(this);
        testNotificaiton.setOnClickListener(this);

        triggerVideoButton.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    triggerValue = dataSnapshot.getValue().toString();
                    switch (triggerValue){
                        case "1" : startActivity(new Intent(getApplicationContext(),TabletVideoActivity.class));
                            triggerVideoButton.setValue(0);
                            break;
                    }
                }catch(Exception e){
                    Log.i("Error","cry");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStop(){
        super.onStop();
        FirebaseAuth.getInstance().signOut();
    }


    @Override
    protected void onMainServiceConnected() {

    }

    @Override
    public void onClick(View view) {
        if (view == logoutButton){
            finish();
            FirebaseAuth.getInstance().signOut();
            //startActivity(new Intent(this,LoginActivity.class));
        }
        if (view == controlButton){
            startActivity(new Intent(this, WheelControlActivity.class));
        }
        if (view == vidButton){
            startActivity(new Intent(this,TabletVideoActivity.class));
        }
        if (view == mapButton){
            startActivity(new Intent(this,IndoorPosition.class));
        }
        if (view == mapButton2){
            startActivity(new Intent(this,IndoorPosition2.class));
        }
        if (view == testButton){
            startActivity(new Intent(this,FaceTrackerActivity.class));
        }
        if (view == testNotificaiton){
            sendNotification();
        }

    }

    public void sendNotification(){
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.common_google_signin_btn_icon_dark))
                .setContentTitle("NotificationTest")
                .setContentText("Hi");

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}