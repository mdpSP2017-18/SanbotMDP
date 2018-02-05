package com.sp.sanbot.sanbotmobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TabControl extends Fragment implements View.OnClickListener {

    Button forwardButton;
    Button backwardButton;
    Button leftButton;
    Button rightButton;
    Button stopbutton;
    Button wander;
    Button stopWander;

    DatabaseReference databaseReferenceF;
    DatabaseReference databaseReferenceB;
    DatabaseReference databaseReferenceL;
    DatabaseReference databaseReferenceR;
    DatabaseReference databaseReferenceS;
    //DatabaseReference databaseReferenceW;
    //DatabaseReference databaseReferenceW2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_control, container, false);
        forwardButton = (Button)view.findViewById(R.id.forwardButton);
        backwardButton = (Button)view.findViewById(R.id.backwardButton);
        leftButton = (Button)view.findViewById(R.id.leftButton);
        rightButton = (Button)view.findViewById(R.id.rightButton);
        stopbutton = (Button)view.findViewById(R.id.stopButton);

        forwardButton.setOnClickListener(this);
        backwardButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        stopbutton.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReferenceF = FirebaseDatabase.getInstance().getReference("Forward");
        databaseReferenceB = FirebaseDatabase.getInstance().getReference("Backward");
        databaseReferenceL = FirebaseDatabase.getInstance().getReference("Left");
        databaseReferenceR = FirebaseDatabase.getInstance().getReference("Right");
        databaseReferenceS = FirebaseDatabase.getInstance().getReference("Stop");
    }

    //TODO: change button setting
    @Override
    public void onClick(View view) {
        if (view == forwardButton){
            databaseReferenceF.setValue(1);
            Log.d("Hi", "forward");
        }else{
            databaseReferenceF.setValue(0);
        }
        if (view == backwardButton){
            databaseReferenceB.setValue(1);
        }
        else{
            databaseReferenceB.setValue(0);
        }
        if (view == leftButton){
            databaseReferenceL.setValue(1);
        }else{
            databaseReferenceL.setValue(0);
        }
        if (view == rightButton){
            databaseReferenceR.setValue(1);
        }else{
            databaseReferenceR.setValue(0);
        }
        if (view == stopbutton){
            databaseReferenceS.setValue(1);
        }else{
            databaseReferenceS.setValue(0);
        }
    }


}
