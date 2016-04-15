package com.ztemt.test.basic;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;

public class TestItemActivity extends Activity {

    private static final String TAG = "TestItemActivity";
    private static final String PREFS = "test_prefs";
    private static final String KEY_SINGLE_MODE = "single_mode";

    private GestureDetector mDetector;

    // Current test item index
    private int mIndex = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetector = new GestureDetector(this, new OnGestureListener());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_item);

        mIndex = getIntent().getIntExtra("ITEM_INDEX", 0);
        setTitle(TestList.get(mIndex).getTestName());
        addFragment(TestList.get(mIndex));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (TestList.get(mIndex).onKeyDown(keyCode, event)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        TestList.get(mIndex).onNewIntent(intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_pass:
            setTestResult(1);
            nextTestItem();
            break;
        case R.id.btn_fail:
            setTestResult(0);
            nextTestItem();
            break;
        }
    }

    private void addFragment(final Fragment newFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.item_fragment, newFragment).commit();
    }

    private void replaceFragment(final Fragment newFragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.item_fragment, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    private void setTestResult(int state) {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        sp.edit().putInt(String.valueOf(mIndex), state).commit();
    }

    private boolean isSingleMode() {
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        return sp.getBoolean(KEY_SINGLE_MODE, false);
    }

    private void nextTestItem() {
        // Next item index
        mIndex++;

        // If have no more item then return
        if (mIndex >= TestList.getCount() || isSingleMode()) {
            setResult(RESULT_OK);
            finish();
        } else {
            setTitle(TestList.get(mIndex).getTestName());
            replaceFragment(TestList.get(mIndex));
        }
    }

    private class OnGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i(TAG, "onDoubleTap on index: " + mIndex);
            return TestList.get(mIndex).onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return TestList.get(mIndex).onSingleTapConfirmed(e);
        }
    }
}
