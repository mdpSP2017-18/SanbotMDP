package com.sp.sanbot.sanbotmobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TabIndoorPosition extends Fragment implements View.OnTouchListener {

    ImageView imageView;
    DatabaseReference tabPositioningX;
    DatabaseReference tabPositioningY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_indoor_position, container, false);
        imageView = view.findViewById(R.id.floorPlan);
        imageView.setOnTouchListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabPositioningX = FirebaseDatabase.getInstance().getReference("TabPositionX");
        tabPositioningY = FirebaseDatabase.getInstance().getReference("TabPositionY");
    }

    public void onDraw(double dX, double dY){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        Bitmap mainImage = BitmapFactory.decodeResource(getResources(), R.drawable.floorplan);
        Bitmap tempBitmap = Bitmap.createBitmap(mainImage.getWidth(), mainImage.getHeight(), Bitmap.Config.RGB_565);

        double width = mainImage.getWidth();
        double height = mainImage.getHeight();
        Log.i("SIZE", "WIDTH/HEIGHT: " + width + " / " + height);

        final Canvas imageCanvas = new Canvas(tempBitmap);
        imageCanvas.drawBitmap(mainImage, 0, 0, null);
        imageCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        imageCanvas.drawBitmap(mainImage, 0, 0, null);
        imageCanvas.drawCircle((float)dX, (float) dY, 50, paint);
        imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int NewX = (int)motionEvent.getX();
        int NewY = (int)motionEvent.getY();
        tabPositioningX.setValue(NewX);
        tabPositioningY.setValue(NewY);
        Log.d("TOUCH", "X/Y: " + NewX + "/" + NewY);
        onDraw(NewX, NewY);
        return false;
    }
}
