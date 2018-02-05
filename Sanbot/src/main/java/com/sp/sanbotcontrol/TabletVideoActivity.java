package com.sp.sanbotcontrol;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sp.motiondetection.MotionDetector;
import com.sp.motiondetection.MotionDetectorCallback;

/**
 * Created by vootl on 17/1/2018.
 */

public class TabletVideoActivity extends AppCompatActivity{

    private MotionDetector motionDetector;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        /*Intent intent = new Intent();
        intent.setComponent(new ComponentName("info.dvkr.screenstream", "info.dvkr.screenstream.ui.StartActivity"));
        startActivity(intent);*/

        databaseReference = FirebaseDatabase.getInstance().getReference("NotificationHehe");

        motionDetector = new MotionDetector(this, (SurfaceView) findViewById(R.id.sfv_videoo));
        motionDetector.setMotionDetectorCallback(new MotionDetectorCallback() {
            @Override
            public void onMotionDetected() {
                Toast.makeText(TabletVideoActivity.this, "Motion detected", Toast.LENGTH_SHORT).show();
                databaseReference.setValue(1);
            }

            @Override
            public void onTooDark() {
                Toast.makeText(TabletVideoActivity.this, "Too dark here", Toast.LENGTH_SHORT).show();
            }
        });

        ////// Config Options
        //motionDetector.setCheckInterval(500);
        //motionDetector.setLeniency(20);
        //motionDetector.setMinLuma(1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        motionDetector.onResume();

        if (motionDetector.checkCameraHardware()) {
            Toast.makeText(TabletVideoActivity.this, "Camera found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TabletVideoActivity.this, "No camera available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        motionDetector.onPause();
    }

}
