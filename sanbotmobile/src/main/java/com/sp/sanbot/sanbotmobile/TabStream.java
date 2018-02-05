package com.sp.sanbot.sanbotmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by vootl on 31/1/2018.
 */

public class TabStream extends Fragment implements View.OnClickListener {

    Button streamButton;
    DatabaseReference triggerVideoButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        triggerVideoButton = FirebaseDatabase.getInstance().getReference("triggerVideo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_stream, container, false);
        streamButton = (Button)view.findViewById(R.id.streamButton);
        streamButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == streamButton) {
            Log.i("StreamTest", "Streaming Started");
            triggerVideoButton.setValue(1);
            //Copy the URL from Screen Stream on Sanbot and paste it in here
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://10.10.10.80:8080"));
            startActivity(browserIntent);
        }
    }
}
