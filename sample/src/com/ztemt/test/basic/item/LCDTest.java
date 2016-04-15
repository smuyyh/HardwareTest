package com.ztemt.test.basic.item;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ztemt.test.basic.R;

public class LCDTest extends BaseTest {

    private static final int[] COLORS = {
        Color.RED,
        Color.GREEN,
        Color.WHITE,
        Color.BLACK,
        Color.BLUE
    };

    private int mColorIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lcd, container, false);
        return v;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        View vc = getView().findViewById(R.id.lcd_color);
        View vt = getView().findViewById(R.id.lcd_text);

        int index = mColorIndex++ % (COLORS.length + 1);

        if (index == 0) {
            setButtonVisibility(false);
            vc.setVisibility(View.VISIBLE);
            vc.setBackgroundColor(COLORS[index]);
            vt.setVisibility(View.GONE);
        } else if (index == COLORS.length) {
            setButtonVisibility(true);
            vc.setVisibility(View.GONE);
            vt.setVisibility(View.VISIBLE);
        } else {
            vc.setBackgroundColor(COLORS[index]);
        }
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.lcd_title);
    }

    @Override
    public boolean isNeedTest() {
        return hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN)
                && getSystemProperties("lcd", true);
    }
}
