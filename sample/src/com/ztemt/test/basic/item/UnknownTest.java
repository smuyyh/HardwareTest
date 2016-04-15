package com.ztemt.test.basic.item;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztemt.test.basic.R;

public class UnknownTest extends BaseTest {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.unknown, container, false);
        return v;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.unknown_title);
    }

    @Override
    public boolean isNeedTest() {
        return false;
    }
}
