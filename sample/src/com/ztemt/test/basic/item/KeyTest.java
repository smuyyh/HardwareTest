package com.ztemt.test.basic.item;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class KeyTest extends BaseTest {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.key, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        TextView v = (TextView) getView().findViewById(R.id.key_text);
        v.setText(KeyEvent.keyCodeToString(keyCode));
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.key_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("key", true);
    }
}
