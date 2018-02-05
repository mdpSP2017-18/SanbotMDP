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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leantegra.wibeat.sdk.monitoring.ScanServiceManager;
import com.leantegra.wibeat.sdk.monitoring.distance.ProximityZone;
import com.leantegra.wibeat.sdk.monitoring.info.BaseFrame;
import com.leantegra.wibeat.sdk.monitoring.info.Region;
import com.leantegra.wibeat.sdk.monitoring.listeners.MonitoringListener;
import com.leantegra.wibeat.sdk.monitoring.listeners.ScanListener;
import com.leantegra.wibeat.sdk.monitoring.listeners.ScanServiceConsumer;
import com.leantegra.wibeat.sdk.monitoring.service.ScanError;

import java.text.DecimalFormat;

import static com.leantegra.wibeat.sdk.monitoring.config.ScanConfig.SCAN_MODE_LOW_LATENCY;
//TODO: Replace AppCompactActivity with TopBaseActivity
public class IndoorPosition extends AppCompatActivity implements ScanServiceConsumer {

    //private static Map<String, List<String>> PLACES_BY_BEACONS;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    TextView textView;
    TextView textView2;
    TextView textView3;
    ImageView imageView1;
    private ScanServiceManager mScanServiceManager;
    double beaconDistance;
    private ArrayMap<String, BaseFrame> mFoundedDeviceMap = new ArrayMap<>();
    Handler handler;
    double aa, bb , cc, dd;
    Button backButton;
    final double q[] = new double[4];
    DatabaseReference databaseReference;
    DatabaseReference databaseDistance;
    DatabaseReference databaseDistance1;
    DatabaseReference databaseDistance2;
    DatabaseReference databaseDistance3;



    double KG, KG1, KG2, KG3;

    double avgX;
    double avgY;
    double avgX1;
    double avgY1;

    double estE = 0;
    double estE1 = 0;
    double estE2 = 0;
    double estE3 = 0;

    double estV = 0;
    double estV1 = 0;
    double estV2 = 0;
    double estV3 = 0;

    double PREestV = 2.5;
    double PREestV1 = 2.5;
    double PREestV2 = 2.5;
    double PREestV3 = 2.5;

    double PREestE = 2;
    double PREestE1 = 2;
    double PREestE2 = 2;
    double PREestE3= 2;
    double measureE = 6;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_position);
        databaseReference = FirebaseDatabase.getInstance().getReference("X");
        databaseDistance = FirebaseDatabase.getInstance().getReference("DistancePink");
        databaseDistance1 = FirebaseDatabase.getInstance().getReference("DistanceYellow");
        databaseDistance2 = FirebaseDatabase.getInstance().getReference("DistancePurple");
        databaseDistance3 = FirebaseDatabase.getInstance().getReference("DistanceA39");
        textView = (TextView)findViewById(R.id.textView);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        imageView1 = (ImageView)findViewById(R.id.imageView);
        backButton = (Button)findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //proximityActivity = new ProximityActivity();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check coarse location permission
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            startRanging();
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
                    startRanging();
                } else {
                    showErrorDialog(getString(R.string.error_permission));
                }
            }
            break;
        }
    }
    private void showErrorDialog(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(IndoorPosition.this);
        builder.setTitle(R.string.error);
        builder.setMessage(error);
        builder.show();
    }


    public void startRanging(){
        mScanServiceManager = new ScanServiceManager(this, this);
        //Set foreground scan period
        mScanServiceManager.setForegroundScanPeriod(5000, 0);
        //Set scan mode
        mScanServiceManager.setScanMode(SCAN_MODE_LOW_LATENCY);

        mScanServiceManager.setScanListener(new ScanListener(){
            @Override
            public void onScanResult(final BaseFrame baseFrame) {
                String key = baseFrame.getBluetoothDevice().getAddress();
                mFoundedDeviceMap.put(key, baseFrame);

                switch (key) {

                    //Pink (Candy)
                    case "C8:E1:0A:72:3D:C7":
                        q[0] = baseFrame.getDistance();
                        textView.setText(String.format("%.1f", q[0]));
                        Log.d("Distance", "Pink: " + q[0]);
                        databaseDistance.setValue(q[0]);
                        KFcal(q[0]);
                        break;

                    //Yellow (Lemon)
                    case "C7:BC:38:B8:1B:92":
                        q[1] = baseFrame.getDistance();
                        textView2.setText(String.format("%.1f", q[1]));
                        Log.d("Distance", "Yellow: " + q[1]);
                        databaseDistance1.setValue(q[1]);
                        KFcal1(q[1]);
                        break;

                    //Purple (Beetroot)
                    case "DD:BC:BA:E7:97:08":
                        q[2] = baseFrame.getDistance();
                        textView3.setText(String.format("%.1f", q[2]));
                        Log.d("Distance", "Purple: " + q[2]);
                        databaseDistance2.setValue(q[2]);
                        KFcal2(q[2]);
                        break;

                    // A39
                    case "01:17:C5:32:7A:39":
                        q[3] = baseFrame.getDistance();
                        //textView3.setText(String.format("%.1f", q[2]));
                        Log.d("Distance", "A39: " + q[3]);
                        databaseDistance3.setValue(q[3]);
                        KFcal3(q[3]);
                        break;
                }

                double x1,x2,x3,x4,x5,x6,y1,y2,y3,y4,y5,y6;
                double h = (8.88 * 8.88);
                double g = (8.08 * 8.08);
                //Log.d("Distance2", "red: " + q[0]);

                if ((q[0] < 12.01 && q[0] != 0) && (q[2] < 12.01 && q[2] != 0) && (q[3] < 12.01 && q[3] != 0) && (q[1] < 12.01 && q[1] != 0)){
                    Log.i("chock","q0/q1: " + q[0] + "/" +q[1]);
                    y4 = ((q[0]*q[0])-(q[1]*q[1])+ g)/16.16;
                    x4 = ((q[0]*q[0])-(q[3]*q[3])+ h)/17.76;
                    x5 = ((q[1]*q[1])-(q[2]*q[2])+ h)/17.76;
                    y5 = ((q[3]*q[3])-(q[2]*q[2])+ g)/16.16;

                    if (q[0]<q[3] || q[1]<q[2]){
                        avgX1 = x4*100;
                        avgY1 = 808+ (y4*100);
                    }else{
                        avgX1 = x5*100;
                        avgY1 = 808 + (y5*100);
                    }
                    Log.i("CHECK","X4/Y4: " + x4 + "/" +y4);

                    y1 = 8.88-((h-(estV1*estV1)+(estV*estV))/17.76);
                    x1 = 8.08-((g-(estV1*estV1)+(estV2*estV2))/16.16);
                    Log.i("RRRR","e1 / e2: " + estV1 + " / " + estV2);
                    x2 = 8.08-((g-(estV*estV)+(estV3*estV3))/16.16);
                    y2 = 8.88-((h-(estV2*estV2)+(estV3*estV3))/17.76);
                    //y3 = 8.88-(((h+g)-(g-h)-(estV1*estV1)+(estV3*estV3)+(estV*estV)-(estV2*estV2))/(2*17.76));
                    //x3 = 8.08-(((h+g)+(g-h)-(estV1*estV1)+(estV3*estV3)-(estV*estV)+(estV2*estV2))/(2*16.16));


                    avgX = (x1 + x2)/2*100;
                    avgY = ((y1 + y2)/2*100)+808;

                    Log.i("Cal", "X = " + avgX + " /Y = " + avgY);
                    Log.i("testcal", "X = " + x1 + " /Y = " + y1);
                    //TODO: Change X and Y
                    getIndoorPosition(avgX, avgY, avgX1, avgY1, x1, x2, y1, y2, estV, estV1, estV2, estV3);//(avgX, avgY, x1, x2, x3, y1, y2, y3, avgX1, avgY1);
                }else{

                }

            }
        });
        mScanServiceManager.bind();
    }

    public double KFcal(double T) {
        //Log.i("asd", "YellowBeacon = Dis:" + T);

        KG = PREestE / (PREestE + measureE);
        estV = PREestV + KG * (T - PREestV);
        estE = (1 - KG) * PREestE;

        /*if (T > estV + 3) {
            estV = estV + 0.02;
        } else if (estV > T + 3) {
            estV = estV - 0.02;
        }*/
        if(PREestE<0.005){
            PREestE = PREestE + 1;
        }

        //estimateTV.setText("Distance :" + df.format(estV));
        //  Log.i("asd", "Estimated Distant:"+ estV);
        Log.i("asd", "Estimated ERROR:" + estE);
        Log.i("asd", "Previous Distant:" + PREestV);
        Log.i("asd", "Previous ERROR:" + PREestE);
        PREestE = estE;
        PREestV = estV;
        Log.i("asd", "Updated Previous Distant:" + PREestV);
        Log.i("asd", "Updated Previous ERROR:" + PREestE);
        return estV;
    }

    public double KFcal1(double T) {
        //Log.i("asd", "YellowBeacon = Dis:" + T);

        KG1 = PREestE1 / (PREestE1 + measureE);
        estV1 = PREestV1 + KG1 * (T - PREestV1);
        estE1 = (1 - KG1) * PREestE1;

        /*if (T > estV1 + 3) {
            estV1 = estV1 + 0.02;
        } else if (estV1 > T + 3) {
            estV1 = estV1 - 0.02;
        }*/
        if(PREestE1<0.005){
            PREestE1 = PREestE1 + 1
            ;
        }

        //estimateTV.setText("Distance :" + df.format(estV));
        //  Log.i("asd", "Estimated Distant:"+ estV);
        Log.i("asd", "Estimated ERROR:" + estE1);
        Log.i("asd", "Previous Distant:" + PREestV1);
        Log.i("asd", "Previous ERROR:" + PREestE1);
        PREestE1 = estE1;
        PREestV1 = estV1;
        Log.i("asd", "Updated Previous Distant:" + PREestV1);
        Log.i("asd", "Updated Previous ERROR:" + PREestE1);
        return estV1;
    }

    public double KFcal2(double T) {
        //Log.i("asd", "YellowBeacon = Dis:" + T);

        KG2 = PREestE2 / (PREestE2 + measureE);
        estV2 = PREestV2 + KG2 * (T - PREestV2);
        estE2 = (1 - KG2) * PREestE2;

        /*if (T > estV2 + 3) {
            estV2 = estV2 + 0.02;
        } else if (estV2 > T + 3) {
            estV2 = estV2 - 0.02;
        }*/
        if(PREestE2<0.005){
            PREestE2 = PREestE2 + 1
            ;
        }

        //estimateTV.setText("Distance :" + df.format(estV));
        //  Log.i("asd", "Estimated Distant:"+ estV);
        Log.i("asd", "Estimated ERROR:" + estE2);
        Log.i("asd", "Previous Distant:" + PREestV2);
        Log.i("asd", "Previous ERROR:" + PREestE2);
        PREestE2 = estE2;
        PREestV2 = estV2;
        Log.i("asd", "Updated Previous Distant:" + PREestV);
        Log.i("asd", "Updated Previous ERROR:" + PREestE);
        return estV2;
    }

    public double KFcal3(double T) {
        //Log.i("asd", "YellowBeacon = Dis:" + T);

        KG3 = PREestE3 / (PREestE3 + measureE);
        estV3 = PREestV3 + KG3 * (T - PREestV3);
        estE3 = (1 - KG3) * PREestE3;

        /*if (T > estV3 + 3) {
            estV3 = estV3 + 0.02;
        } else if (estV3 > T + 3) {
            estV3 = estV3 - 0.02;
        }*/
        if(PREestE3<0.005){
            PREestE3 = PREestE3 + 1
            ;
        }

        //estimateTV.setText("Distance :" + df.format(estV));
        //  Log.i("asd", "Estimated Distant:"+ estV);
        Log.i("asd", "Estimated ERROR:" + estE3);
        Log.i("asd", "Previous Distant:" + PREestV3);
        Log.i("asd", "Previous ERROR:" + PREestE3);
        PREestE3 = estE3;
        PREestV3 = estV3;
        Log.i("asd", "Updated Previous Distant:" + PREestV3);
        Log.i("asd", "Updated Previous ERROR:" + PREestE3);
        return estV3;
    }

    public void getIndoorPosition(double avgX, double avgY, double avgX1, double avgY1, double x1, double x2, double y1, double y2, double estV, double
            estV1, double estV2, double estV3){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        Paint paint1 = new Paint();
        paint1.setColor(Color.BLACK);
        paint1.setStyle(Paint.Style.FILL);

        Paint paintYellow = new Paint();
        paintYellow.setColor(Color.YELLOW);
        paintYellow.setStrokeWidth(2);
        paintYellow.setStyle(Paint.Style.STROKE);

        Paint paintPink = new Paint();
        paintPink.setColor(Color.MAGENTA);
        paintPink.setStrokeWidth(2);
        paintPink.setStyle(Paint.Style.STROKE);

        Paint paintPurple = new Paint();
        paintPurple.setColor(Color.BLACK);
        paintPurple.setStrokeWidth(2);
        paintPurple.setStyle(Paint.Style.STROKE);

        Paint paintA39 = new Paint();
        paintA39.setColor(Color.BLUE);
        paintA39.setStrokeWidth(2);
        paintA39.setStyle(Paint.Style.STROKE);

        Bitmap mainImage = BitmapFactory.decodeResource(getResources(), R.drawable.floorplan);
        Bitmap tempBitmap = Bitmap.createBitmap(mainImage.getWidth(), mainImage.getHeight(), Bitmap.Config.RGB_565);
        double width = mainImage.getWidth();
        double height = mainImage.getHeight();
        Log.i("SIZE", "WIDTH/HEIGHT: " + width + " / " + height);
        final Canvas imageCanvas = new Canvas(tempBitmap);
        imageCanvas.drawBitmap(mainImage, 0, 0, null);
        imageCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        imageCanvas.drawBitmap(mainImage, 0, 0, null);

        imageCanvas.drawCircle((float)avgX, (float) avgY, 50, paint);
        imageCanvas.drawCircle((float)avgX1, (float) avgY1, 50, paint1);

        imageCanvas.drawCircle(0,1616, (float)(estV1*100), paintYellow);
        imageCanvas.drawCircle(0,808, (float)(estV*100), paintPink);
        imageCanvas.drawCircle(888,1616, (float)(estV2*100), paintPurple);
        imageCanvas.drawCircle(888,808, (float)(estV3*100), paintA39);
        //imageView1.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

        imageView1.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mScanServiceManager != null) {
            //Stop scan
            mScanServiceManager.stopScan();
            //Stop scan service
            mScanServiceManager.unbind();
            mScanServiceManager = null;
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBind() {

    }

    @Override
    public void onUnbind() {

    }

    @Override
    public void onError(ScanError scanError) {
        showErrorDialog(scanError.toString());
    }

    /*public void startMonitoring(){
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
                Toast.makeText(IndoorPosition.this, "Enter Region" + region.getRegionId(), Toast.LENGTH_SHORT).show();
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
    }*/
}
