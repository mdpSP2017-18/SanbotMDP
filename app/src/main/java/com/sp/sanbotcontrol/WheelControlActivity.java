package com.sp.sanbotcontrol;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.qihancloud.opensdk.base.TopBaseActivity;
import com.qihancloud.opensdk.beans.FuncConstant;
import com.qihancloud.opensdk.function.beans.wheelmotion.DistanceWheelMotion;
import com.qihancloud.opensdk.function.beans.wheelmotion.RelativeAngleWheelMotion;
import com.qihancloud.opensdk.function.unit.ModularMotionManager;
import com.qihancloud.opensdk.function.unit.WheelMotionManager;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_control);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

        forwardButton.setOnClickListener(this);
        backwardButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        backbutton.setOnClickListener(this);
        stopbutton.setOnClickListener(this);
        wander.setOnClickListener(this);
        stopWander.setOnClickListener(this);
    }


    @Override
    protected void onMainServiceConnected() {

    }

    @Override
    public void onClick(View view) {

        if (view == forwardButton) {
            goForward();
        }
        if (view == backwardButton) {
            goBackward();
            Log.e("TEST", "HI");
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
            //startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        if (view == wander){
            goWander();
        }
        if (view == stopWander){
            stopWander();
        }


    }

    public void goForward() {
        //DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN, 5, 100);
        DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_FORWARD_RUN,  5,100);

        wheelMotionManager.doDistanceMotion(distanceWheelMotion);
    }

    public void goBackward() {
        DistanceWheelMotion distanceWheelMotion = new DistanceWheelMotion(DistanceWheelMotion.ACTION_BACK_RUN, 5,100);
        wheelMotionManager.doDistanceMotion(distanceWheelMotion);

    }

    public void goLeft() {
        RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_LEFT, 5, 90);
        wheelMotionManager.doRelativeAngleMotion(relativeAngleWheelMotion);
    }

    public void goRight() {
        RelativeAngleWheelMotion relativeAngleWheelMotion = new RelativeAngleWheelMotion(RelativeAngleWheelMotion.TURN_RIGHT, 5, 90);
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
}
