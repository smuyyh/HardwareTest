package com.ztemt.test.basic.item;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ztemt.test.basic.R;

public class CameraTest extends BaseTest {

    private static final String IMG_NAME = "test.jpg";
    private static final String EXTRAS_CAMERA_FACING = "android.intent.extras.CAMERA_FACING";
    private static final int REQUEST_CAPTURE_IMAGE = 1;

    private int mCameraId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.camera, container, false);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && mCameraId < Camera.getNumberOfCameras() - 1) {
            mCameraId++;
            captureImage();
        } else {
            setButtonVisibility(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Delete image file in storage
        File file = new File(Environment.getExternalStorageDirectory(), IMG_NAME);
        file.delete();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mCameraId = 0;
        captureImage();
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.camera_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        return hasCamera && getSystemProperties("camera", true);
    }

    private void captureImage() {
        File file = new File(Environment.getExternalStorageDirectory(), IMG_NAME);
        Uri uri = Uri.fromFile(file);

        // Wrap capture intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(EXTRAS_CAMERA_FACING, mCameraId);

        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }
}
