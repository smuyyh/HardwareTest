package com.ztemt.test.basic.item;

import com.ztemt.test.basic.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class VibratorTest extends BaseTest implements OnClickListener {

    private Vibrator mVibrator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vibrator, container, false);
        v.findViewById(R.id.btn_long_vibrator).setOnClickListener(this);
        v.findViewById(R.id.btn_short_vibrator).setOnClickListener(this);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        setButtonVisibility(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btn_long_vibrator:
            mVibrator.vibrate(1000);
            break;
        case R.id.btn_short_vibrator:
            mVibrator.vibrate(45);
            break;
        }
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.vibrator_title);
    }

    @Override
    public boolean isNeedTest() {
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        return v != null && v.hasVibrator() && getSystemProperties("vibrator", true);
    }
}
