package com.sp.motiondetection;

public interface MotionDetectorCallback {
    void onMotionDetected();
    void onTooDark();
}
