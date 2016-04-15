package com.ztemt.test.basic.item;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class CompassTest extends BaseTest implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccSensor;
    private Sensor mMagSensor;

    private CompassView mView;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float[] mR = new float[16];
    private float[] mValues = new float[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.compass, container, false);
        mView = (CompassView) v.findViewById(R.id.compass_view);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Get sensors
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, mMagSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            if (accuracy > SensorManager.SENSOR_STATUS_ACCURACY_LOW) {
                setCompassText(R.string.compass_check_point);
            } else {
                setCompassText(R.string.compass_calibration);
            }
            setCompassAccuracy(accuracy);
        }
    }

    private void setCompassText(int resId) {
        ((TextView) getView().findViewById(R.id.compass_text)).setText(resId);
    }

    private void setCompassAccuracy(int accuracy) {
        TextView v = (TextView) getView().findViewById(R.id.compass_accuracy);
        v.setText(getString(R.string.compass_accuracy, accuracy));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();
        float[] data;

        if (type == Sensor.TYPE_MAGNETIC_FIELD) {
            data = mGeomagnetic;
        } else if (type == Sensor.TYPE_ACCELEROMETER) {
            data = mGravity;
        } else {
            return;
        }

        for (int i = 0; i < 3; i++) {
            data[i] = event.values[i];
        }

        SensorManager.getRotationMatrix(mR, null, mGravity, mGeomagnetic);
        SensorManager.getOrientation(mR, mValues);

        float degree = -(float) Math.toDegrees(mValues[0]);
        mView.setDegree(degree);
        mView.invalidate();
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        setButtonVisibility(!isButtonVisible());
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.compass_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasCompass = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);
        return hasCompass && getSystemProperties("compass", true);
    }

    public static class CompassView extends View {

        private Paint mPaint = new Paint();
        private Path mPath = new Path();

        private float mDensity;
        private float mDegree;

        public CompassView(Context context) {
            this(context, null);
        }

        public CompassView(Context context, AttributeSet attrs) {
            super(context, attrs);

            // Paint
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.WHITE);

            // Density
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            mDensity = dm.density;

            // Construct a wedge-shaped path
            float scale = (float) (mDensity / 1.5);
            mPath.moveTo(0, -50 * scale);
            mPath.lineTo(-20 * scale, 60 * scale);
            mPath.lineTo(0, 50 * scale);
            mPath.lineTo(20 * scale, 60 * scale);
            mPath.close();
        }

        public void setDegree(float degree) {
            mDegree = degree;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int h = canvas.getHeight();
            int x = canvas.getWidth() / 2;
            int y = h / 2;

            canvas.drawColor(Color.BLACK);
            canvas.translate(x, y);
            canvas.rotate(mDegree);
            canvas.drawPath(mPath, mPaint);
        }
    }
}
