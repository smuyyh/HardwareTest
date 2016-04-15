package com.ztemt.test.basic.item;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class LightSensorTest extends BaseTest implements SensorEventListener {

    private static final int MSG_FAIL = 1;
    private static final int MSG_PASS = 2;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private View mFirView;
    private View mPreView;
    private View mCurView;

    private int mSignalValue = 0;
    private boolean mPause;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Reset!
        mFirView = null;
        mPreView = null;
        mCurView = null;
        mPause = false;

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        setTimerTask(MSG_FAIL, 6000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lightsensor, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not to do!
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT && !mPause) {
            mSignalValue = (int) event.values[0];
            setSignalText(mSignalValue);
            onBrightnessChanged(event.values[0]);
        }
    }

    @Override
    public void onHandleMessage(final int index) {
        if (index == MSG_FAIL && mSignalValue <= 0) {
            clickFailButton();
        } else if (index == MSG_PASS) {
            clickPassButton();
        }
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.lightsensor_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasLightSensor = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
        return hasLightSensor && getSystemProperties("lightsensor", true);
    }

    private void setSignalText(int value) {
        TextView v = (TextView) getView().findViewById(R.id.lightsensor_signal);
        v.setText(getString(R.string.lightsensor_signal, value));
    }

    private void onBrightnessChanged(float brightness) {
        if (mPreView != null) {
            mPreView.setBackgroundColor(Color.BLUE);
        }

        if (brightness > 0 && brightness <= 20) {
            mCurView = getView().findViewById(R.id.lightsensor_level_1);
        } else if (brightness > 20 && brightness <= 1000) {
            mCurView = getView().findViewById(R.id.lightsensor_level_2);
        } else {
            mCurView = getView().findViewById(R.id.lightsensor_level_3);
        }

        mCurView.setBackgroundColor(Color.GREEN);

        if (mFirView == null) {
            mFirView = mCurView;
        } else if (mFirView == mCurView && mPreView != mCurView) {
            setTimerTask(MSG_PASS, 300);
            mPause = true;
        }

        mPreView = mCurView;
    }
}
