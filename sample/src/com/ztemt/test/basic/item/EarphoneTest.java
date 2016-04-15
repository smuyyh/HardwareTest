package com.ztemt.test.basic.item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class EarphoneTest extends LoopbackTest {

    private static final String TAG = "EarphoneTest";

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                int state = intent.getIntExtra("state", 0);

                // Prompt press hook if plug in
                if (state == 1) {
                    setStepText(R.string.earphone_press_hook);
                    startLoopback();
                } else {
                    setStepText(R.string.earphone_plug_in);
                    stopLoopback();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.earphone, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_HEADSETHOOK:
            setButtonVisibility(true);
            return true;
        default:
            Log.d(TAG, "onKeyDown -> keyCode: " + keyCode);
            return false;
        }
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.earphone_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("earphone", true);
    }

    private void setStepText(int resId) {
        ((TextView) getView().findViewById(R.id.earphone_text)).setText(resId);
    }
}
