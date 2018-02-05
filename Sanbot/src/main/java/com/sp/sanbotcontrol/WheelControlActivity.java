package com.sp.sanbotcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.HardWareManager;
import com.qihancloud.opensdk.function.unit.ModularMotionManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;

import java.util.List;

public class WheelControlActivity extends TopBaseActivity implements View.OnClickListener {

    Button forwardButton;
    Button backwardButton;
    Button leftButton;
    Button rightButton;
    Button backbutton;
    Button stopbutton;
    Button wander;
    Button stopWander;
    WheelMotionManager wheelMotionManager;
    ModularMotionManager modularMotionManager;
    //FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenceF;
    DatabaseReference databaseReferenceB;
    DatabaseReference databaseReferenceL;
    DatabaseReference databaseReferenceR;
    DatabaseReference databaseReferenceS;
    HardWareManager hardWareManager;

    String valueForward;
    String valueBackward;
    String valueLeft;
    String valueRight;
    private List<String> command ;



    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_control);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReferenceF = FirebaseDatabase.getInstance().getReference("Forward");
        databaseReferenceB = FirebaseDatabase.getInstance().getReference("Backward");
        databaseReferenceL = FirebaseDatabase.getInstance().getReference("Left");
        databaseReferenceR = FirebaseDatabase.getInstance().getReference("Right");
        databaseReferenceS = FirebaseDatabase.getInstance().getReference("Stop");

        forwardButton = (Button) findViewById(R.id.forwardButton);
        backwardButton = (Button) findViewById(R.id.backwardButton);
        leftButton = (Button) findViewById(R.id.leftButton);
        rightButton = (Button) findViewById(R.id.rightButton);
        backbutton = (Button) findViewById(R.id.back_btn1);
        stopbutton = (Button) findViewById(R.id.stopButton);
        wander = (Button)findViewById(R.id.hehe);
        stopWander = (Button)findViewById(R.id.hehe2);
        wheelMotionManager = (WheelMotionManager) getUnitManager(FuncConstant.WHEELMOTION_MANAGER);
        modularMotionManager= (ModularMotionManager)getUnitManager(FuncConstant. MODULARMOTION_MANAGER);
        hardWareManager = (HardWareManager)getUnitManager (FuncConstant.HARDWARE_MANAGER);
        handler = new Handler();


        forwardButton.setOnClickListener(this);
        backwardButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backbutton.setOnClickListener(this);
        stopbutton.setOnClickListener(this);
        wander.setOnClickListener(this);
        stopWander.setOnClickListener(this);

        getData();
    }


    public void getData(){
        databaseReferenceF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valueForward = dataSnapshot.getValue().toString();
                switch (valueForward){
                    case "1" : handler.post(runnableF);
                        break;
                    case "0" : Log.i("VALUE","Forward: " +valueForward);
                        handler.removeCallbacks(runnableF);
                        break;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });

        databaseReferenceB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valueBackward = dataSnapshot.getValue().toString();
                switch (valueBackward) {
                    case "1": handler.post(runnableB);
                        break;
                    case "0": Log.i("VALUE", "Backward: " + valueBackward);
                        handler.removeCallbacks(runnableB);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });

        databaseReferenceL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valueLeft = dataSnapshot.getValue().toString();
                switch (valueLeft) {
                    case "1":handler.post(runnableL);
                        break;
                    case "0": Log.i("VALUE", "Left: " + valueLeft);
                        handler.removeCallbacks(runnableL);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });

        databaseReferenceR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valueRight = dataSnapshot.getValue().toString();
                switch (valueRight) {
                    case "1":handler.post(runnableR);
                        break;
                    case "0":Log.i("VALUE", "Right: " + valueRight);
                        handler.removeCallbacks(runnableR);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });

        databaseReferenceS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valueStop = dataSnapshot.getValue().toString();
                Log.i("VALUE","Stop: " +valueStop);
                switch (valueStop){
                    case "1" : Log.i("VALUE","Stop: " +valueStop);
                        stop();
                        break;
                    case "0" : Log.i("VALUE","Stop: " +valueStop);
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
    }


    @Override
    protected void onMainServiceConnected() {

    }

    /*private void move(){
        int i ;
        command = Arrays.asList("a", "b", "c" , "d");
        Log.i("test", "TEST");
        for( i= 0 ; i< command.size(); i ++ ){
            if (command.size() >= i) {
                String move = command.get(i);
                Log.i("test", "command value :" + move);

                if (move.contains("a")) {
                    Log.i("test", "A OK" );
                    goRight();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else if (move.contains("b")) {
                    Log.i("test", "B OK" );
                    goLeft();

                } else if (move.contains("c")) {
                    Log.i("test", "C OK");

                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }*/


    @Override
    public void onClick(View view) {

        if (view == forwardButton) {
            //Log.i("test", "TEST");
            goLeft();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            goRight();

        }
        if (view == backwardButton) {

        }
        if (view == leftButton) {
            goLeft();
        }
        if (view == rightButton) {
            goRight();
        }
        if (view == stopbutton) {
            stop();
        }
        if (view == backbutton) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (view == wander){
            goWander();
        }
        if (view == stopWander){
            stopWander();
        }
    }

    final Runnable runnableF = new Runnable() {
        @Override
        public void run() {
            goForward();
            Log.i("VALUE","FORWARD: " +valueForward);
            handler.postDelayed(this,1000);
        }
    };

    final Runnable runnableB = new Runnable() {
        @Override
        public void run() {
            goBackward();
            Log.i("VALUE","BACKWARD: " +valueBackward);
            handler.postDelayed(this,1000);
        }
    };

    final Runnable runnableR = new Runnable() {
        @Override
        public void run() {
            goRight();
            Log.i("VALUE","RIGHT: " +valueRight);
            handler.postDelayed(this,1000);

        }
    };

    final Runnable runnableL= new Runnable() {
        @Override
        public void run() {
            goLeft();
            Log.i("VALUE","LEFT: " +valueLeft);
            handler.postDelayed(this,1000);
        }
    };

    public void goForward() {
        DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 8,20);
        wheelMotionManager.doDistanceMotion(distanceWheelMotion);
    }

    public void goBackward() {
        DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_BACK_RUN, 8,20);
        wheelMotionManager.doDistanceMotion(distanceWheelMotion);
    }

    public void goLeft() {
        RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 8, 88);
        wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
    }

    public void goRight() {
        RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_RIGHT, 8, 90);
        wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);

    }

    public void stop() {
        DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_STOP_RUN, 5, 100);
        wheelMotionManager.doDistanceMotion(distanceWheelMotion);
    }

    public void goWander(){
        modularMotionManager.switchWander(true);
    }

    public void stopWander(){
        modularMotionManager.switchWander(false);
    }




    @Override
    public void onDestroy (){
        super.onDestroy();
        handler.removeCallbacks(runnableR);
        handler.removeCallbacks(runnableL);
        handler.removeCallbacks(runnableF);
        handler.removeCallbacks(runnableB);
    }
}

