package com.ztemt.test.basic.item;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.ztemt.test.basic.R;

public abstract class BaseTest extends Fragment implements Runnable {

    private Context mContext;
    private ScheduledThreadPoolExecutor mTimerTask;
    private int mIndex;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            onHandleMessage(msg.arg1);
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimerTask();
    }

    @Override
    public void run() {
        Message message = mHandler.obtainMessage();
        message.arg1 = mIndex;
        mHandler.sendMessage(message);
    }

    public void setTimerTask(final int index, int delay) {
        cancelTimerTask();

        mIndex = index;
        mTimerTask = new ScheduledThreadPoolExecutor(10);
        mTimerTask.schedule(this, delay, TimeUnit.MILLISECONDS);
    }

    public void cancelTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.remove(this);
            mTimerTask.shutdownNow();
            mTimerTask = null;
        }
    }

    /** Handle the test step */
    public void onHandleMessage(final int index) {
        
    }

    /** Get the test name */
    public abstract String getTestName();

    /** Whether you need to test */
    public abstract boolean isNeedTest();

    /** Key press down handler */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /** New intent handler */
    public void onNewIntent(Intent intent) {

    }

    /** Double tap handler */
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    /** Single tap confirmed handler */
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    /** Get the system properties */
    protected boolean getSystemProperties(String name, boolean def) {
        return SystemProperties.getBoolean("debug.test.basic." + name, def);
    }

    protected boolean hasSystemFeature(String name) {
        return getContext().getPackageManager().hasSystemFeature(name);
    }

    /** Set button visible or gone */
    protected void setButtonVisibility(boolean visible) {
        getActivity().findViewById(R.id.buttons).setVisibility(
                visible ? View.VISIBLE : View.GONE);
    }

    /** Get button visibility */
    protected boolean isButtonVisible() {
        return getActivity().findViewById(R.id.buttons).getVisibility() == View.VISIBLE;
    }

    /** Perform pass button click */
    protected void clickPassButton() {
        getActivity().findViewById(R.id.btn_pass).performClick();
    }

    /** Perform fail button click */
    protected void clickFailButton() {
        getActivity().findViewById(R.id.btn_fail).performClick();
    }
}
