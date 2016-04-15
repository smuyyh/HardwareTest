package com.ztemt.test.basic.item;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztemt.test.basic.R;

public class GyroSensorTest extends BaseTest {

    private SensorManager mSensorManager;
    private GyroSurfaceView mView;
    private MyRenderer mRenderer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gyrosensor, container, false);
        mView = (GyroSurfaceView) v.findViewById(R.id.gyro_view);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mRenderer = new MyRenderer();
        mView.setRenderer(mRenderer);
        mRenderer.start();
        mView.onResume();

        setButtonVisibility(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRenderer.stop();
        mView.onPause();
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.gyrosensor_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasGyroSensor = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
        return hasGyroSensor && getSystemProperties("gyrosensor", true);
    }

    public static class GyroSurfaceView extends GLSurfaceView {

        public GyroSurfaceView(Context context) {
            this(context, null);
        }

        public GyroSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

    class MyRenderer implements GLSurfaceView.Renderer, SensorEventListener {

        private Cube mCube;
        private Sensor mRotationVectorSensor;
        private final float[] mRotationMatrix = new float[16];

        public MyRenderer() {
            // find the rotation-vector sensor
            mRotationVectorSensor = mSensorManager.getDefaultSensor(
                    Sensor.TYPE_ROTATION_VECTOR);

            mCube = new Cube();

            // initialize the rotation matrix to identity
            mRotationMatrix[ 0] = 1;
            mRotationMatrix[ 4] = 1;
            mRotationMatrix[ 8] = 1;
            mRotationMatrix[12] = 1;
        }

        public void start() {
            // enable our sensor when the activity is resumed, ask for
            // 10 ms updates
            mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
        }

        public void stop() {
            // make sure to turn our sensor off when the activity is paused
            mSensorManager.unregisterListener(this);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // clear screen
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // set-up modelview matrix
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -3.0f);
            gl.glMultMatrixf(mRotationMatrix, 0);

            // draw our object
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            mCube.draw(gl);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // set view-port
            gl.glViewport(0, 0, width, height);
            // set projection matrix
            float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // dither is enabled by default, we don't need it
            gl.glDisable(GL10.GL_DITHER);
            // clear screen in black
            gl.glClearColor(0, 0, 0, 1);
        }

        @Override
        public void onAccuracyChanged(Sensor arg0, int arg1) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // we received a sensor event. it is a good practice to check
            // that we received the proper event
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                // convert the rotation-vector to a 4x4 matrix. the matrix
                // is interpreted by Open GL as the inverse of the
                // rotation-vector, which is what we want.
                SensorManager.getRotationMatrixFromVector(
                        mRotationMatrix, event.values);
            }
        }

        class Cube {
            // initialize our cube
            private FloatBuffer mVertextBuffer;
            private FloatBuffer mColorBuffer;
            private ByteBuffer mIndexBuffer;

            public Cube() {
                final float vertices[] = {
                        -1, -1, -1,  1, -1, -1,
                         1,  1, -1, -1,  1, -1,
                        -1, -1,  1,  1, -1,  1,
                         1,  1,  1, -1,  1,  1
                };

                final float colors[] = {
                        0, 0, 0, 1, 1, 0, 0, 1,
                        1, 1, 0, 1, 0, 1, 0, 1,
                        0, 0, 1, 1, 1, 0, 1, 1,
                        1, 1, 1, 1, 0, 1, 1, 1
                };

                final byte indices[] = {
                        0, 4, 5, 0, 5, 1,
                        1, 5, 6, 1, 6, 2,
                        2, 6, 7, 2, 7, 3,
                        3, 7, 4, 3, 4, 0,
                        4, 7, 6, 4, 6, 5,
                        3, 0, 1, 3, 1, 2
                };

                ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
                vbb.order(ByteOrder.nativeOrder());
                mVertextBuffer = vbb.asFloatBuffer();
                mVertextBuffer.put(vertices);
                mVertextBuffer.position(0);

                ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
                cbb.order(ByteOrder.nativeOrder());
                mColorBuffer = cbb.asFloatBuffer();
                mColorBuffer.put(colors);
                mColorBuffer.position(0);

                mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
                mIndexBuffer.put(indices);
                mIndexBuffer.position(0);
            }

            public void draw(GL10 gl) {
                gl.glEnable(GL10.GL_CULL_FACE);
                gl.glFrontFace(GL10.GL_CW);
                gl.glShadeModel(GL10.GL_SMOOTH);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertextBuffer);
                gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
                gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
            }
        }
    }
}
