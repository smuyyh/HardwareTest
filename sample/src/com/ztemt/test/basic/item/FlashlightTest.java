package com.ztemt.test.basic.item;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ztemt.test.basic.R;

public class FlashlightTest extends BaseTest {

    private static final String TAG = "FlashlightTest";
    private static final int MSG_START = 1;
    private static final int MSG_END   = 2;
    private static final int MSG_DELAY_TIME = 2000;

    private Camera mCamera;
    private Camera.Parameters mParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.flashlight, container, false);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        turnOff();
    }

    @Override
    public void onHandleMessage(final int index) {
        switch (index) {
        case MSG_START:
            setTimerTask(MSG_END, MSG_DELAY_TIME);
            setButtonVisibility(false);
            turnOn();
            break;
        case MSG_END:
            setButtonVisibility(true);
            turnOff();
            break;
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        setTimerTask(MSG_START, 0);
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.flashlight_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasFlashlight = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        return hasFlashlight && getSystemProperties("flashlight", true);
    }

    private void turnOn() {
        try {
            mCamera = Camera.open();
            mParams = mCamera.getParameters();
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParams);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Turn on flashlight fail.", e);
        }
    }

    private void turnOff() {
        if (mCamera == null) {
            return;
        }

        try {
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(mParams);
            mCamera.stopPreview();
            mCamera.release();
        } catch (Exception e) {
            Log.e(TAG, "Turn off flashlight fail.", e);
        } finally {
            mCamera = null;
        }
    }
}
