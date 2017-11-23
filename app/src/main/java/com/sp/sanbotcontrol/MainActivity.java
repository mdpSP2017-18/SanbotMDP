package com.sp.sanbotcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.qihancloud.opensdk.base.TopBaseActivity;

public class MainActivity extends TopBaseActivity implements View.OnClickListener{

    Button logoutButton;
    Button controlButton;
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        logoutButton = (Button)findViewById(R.id.logoutButton);
        controlButton = (Button)findViewById(R.id.controlButon);
        firebaseAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(this);
        controlButton.setOnClickListener(this);
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
    }


}