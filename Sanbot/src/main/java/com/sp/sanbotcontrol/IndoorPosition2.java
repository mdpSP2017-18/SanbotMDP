package com.sp.sanbotcontrol;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leantegra.wibeat.sdk.monitoring.ScanServiceManager;
import com.leantegra.wibeat.sdk.monitoring.distance.ProximityZone;
import com.leantegra.wibeat.sdk.monitoring.info.BaseFrame;
import com.leantegra.wibeat.sdk.monitoring.info.Region;
import com.leantegra.wibeat.sdk.monitoring.listeners.MonitoringListener;
import com.leantegra.wibeat.sdk.monitoring.listeners.ScanServiceConsumer;
import com.leantegra.wibeat.sdk.monitoring.service.ScanError;

import static com.leantegra.wibeat.sdk.monitoring.config.ScanConfig.SCAN_MODE_LOW_LATENCY;

/**
 * Created by vootl on 31/1/2018.
 */

public class IndoorPosition2 extends AppCompatActivity implements ScanServiceConsumer {

    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    DatabaseReference databaseReference;
    ImageView imageView1;
    double aa, bb , cc, dd;
    private ScanServiceManager mScanServiceManager;
    private ArrayMap<String, BaseFrame> mFoundedDeviceMap = new ArrayMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_position2);
        databaseReference = FirebaseDatabase.getInstance().getReference("X");
        imageView1 = (ImageView)findViewById(R.id.imageView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check coarse location permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            startMonitoring();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startMonitoring();
                } else {
                    showErrorDialog(getString(R.string.error_permission));
                }
            }
            break;
        }
    }
    private void showErrorDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(IndoorPosition2.this);
        builder.setTitle(R.string.error);
        builder.setMessage(error);
        builder.show();
    }

    public void startMonitoring(){
        mScanServiceManager = new ScanServiceManager(this, this);
        mScanServiceManager.setForegroundScanPeriod(5000, 0);
        mScanServiceManager.setScanMode(SCAN_MODE_LOW_LATENCY);

        //Pink
        mScanServiceManager.addMonitoringRegion(new Region.Builder(1).addAddress("C8:E1:0A:72:3D:C7")
                .setProximityZone(ProximityZone.NEAR).build());
        //Yellow
        mScanServiceManager.addMonitoringRegion(new Region.Builder(2).addAddress("C7:BC:38:B8:1B:92")
                .setProximityZone(ProximityZone.NEAR).build());
        //Purple
        mScanServiceManager.addMonitoringRegion(new Region.Builder(3).addAddress("DD:BC:BA:E7:97:08")
                .setProximityZone(ProximityZone.NEAR).build());
        //A39
        mScanServiceManager.addMonitoringRegion(new Region.Builder(4).addAddress("01:17:C5:32:7A:39")
                .setProximityZone(ProximityZone.NEAR).build());

        mScanServiceManager.setMonitoringListener(new MonitoringListener() {
            @Override
            public void onEnterRegion(Region region, BaseFrame baseFrame) {
                Toast.makeText(IndoorPosition2.this, "Enter Region" + region.getRegionId(), Toast.LENGTH_SHORT).show();
                //beaconDistance = baseFrame.getDistance();
                //Log.d("HI", "Distance = " + beaconDistance);

                //Pink
                if (region.getRegionId() == 1){
                    databaseReference.setValue(1);
                    aa = 0;
                    bb = 808;
                    cc = 444;
                    dd = 1616;
                }

                //Yellow
                if (region.getRegionId() == 2){
                    databaseReference.setValue(2);
                    aa = 0;
                    bb = 0;
                    cc = 444;
                    dd = 808;
                }

                //Purple
                if (region.getRegionId() == 3){
                    databaseReference.setValue(3);
                    aa = 444;
                    bb = 0;
                    cc = 888;
                    dd = 808;
                }

                //A39
                if (region.getRegionId() == 4){
                    databaseReference.setValue(4);
                    aa = 444;
                    bb = 808;
                    cc = 888;
                    dd = 1616;
                }
                getIndoorPosition2(aa,bb,cc,dd);
            }


            @Override
            public void onExitRegion(Region region, BaseFrame baseFrame) {
                //Toast.makeText(IndoorPosition.this, "Exit Region", Toast.LENGTH_SHORT).show();
            }
        });

        mScanServiceManager.bind();
    }

    public void getIndoorPosition2(double aa,double bb,double cc,double dd) {
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

        imageCanvas.drawRect((float)aa,(float)bb,(float)cc,(float)dd,paint);

        imageView1.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

    @Override
    public void onBind() {

    }

    @Override
    public void onUnbind() {

    }

    @Override
    public void onError(ScanError scanError) {

    }
}
