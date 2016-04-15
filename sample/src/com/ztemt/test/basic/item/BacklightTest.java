package com.ztemt.test.basic.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class BacklightTest extends BaseTest {

    private static final int MSG_BACKLIGHT_ON    = 1;
    private static final int MSG_BACKLIGHT_OFF   = 2;
    private static final int MSG_BACKLIGHT_DEF   = 3;
    private static final int MSG_BACKLIGHT_END   = 4;
    private static final int MSG_DELAY_TIME   = 3000;

    private float mDefaultBrightness;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.backlight, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDefaultBrightness = getActivity().getWindow().getAttributes().screenBrightness;
    }

    @Override
    public void onHandleMessage(final int index) {
        switch (index) {
        case MSG_BACKLIGHT_ON:
            setButtonVisibility(false);
            setStepText(R.string.backlight_on);
            setScreenBrightness(1.0f);
            setTimerTask(MSG_BACKLIGHT_OFF, MSG_DELAY_TIME);
            break;
        case MSG_BACKLIGHT_OFF:
            setStepText(R.string.backlight_off);
            setScreenBrightness(0.1f);
            setTimerTask(MSG_BACKLIGHT_DEF, MSG_DELAY_TIME);
            break;
        case MSG_BACKLIGHT_DEF:
            setStepText(R.string.backlight_def);
            setScreenBrightness(mDefaultBrightness);
            setTimerTask(MSG_BACKLIGHT_END, MSG_DELAY_TIME);
            break;
        case MSG_BACKLIGHT_END:
            setButtonVisibility(true);
            setStepVisibility(false);
            break;
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        setTimerTask(MSG_BACKLIGHT_ON, 0);
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.backlight_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("backlight", true);
    }

    private void setScreenBrightness(float brightness) {
        WindowManager.LayoutParams p = getActivity().getWindow().getAttributes();
        p.screenBrightness = brightness;
        getActivity().getWindow().setAttributes(p);
    }

    private void setStepVisibility(boolean visible) {
        getView().findViewById(R.id.backlight_step).setVisibility(
                visible ? View.VISIBLE : View.GONE);
        getView().findViewById(R.id.backlight_text).setVisibility(
                visible ? View.GONE : View.VISIBLE);
    }

    private void setStepText(int resId) {
        ((TextView) getView().findViewById(R.id.backlight_step)).setText(resId);
        setStepVisibility(true);
    }
}
