package com.ztemt.test.basic.item;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ztemt.test.basic.R;

public class FmRadioTest extends BaseTest {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fmradio, container, false);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setButtonVisibility(true);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Intent intent = new Intent("com.ztemt.factoryfmtest.ENTRY_ACTION");
        try {
            startActivityForResult(intent, 0);
            setButtonVisibility(false);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), R.string.fmradio_notfound,
                    Toast.LENGTH_SHORT).show();
            setButtonVisibility(true);
        }
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.fmradio_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("fmradio", true);
    }
}
