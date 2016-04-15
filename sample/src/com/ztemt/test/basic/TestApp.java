package com.ztemt.test.basic;

import android.app.Application;

public class TestApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Update test items when the app starts.
        TestList.updateItems(getBaseContext());
    }
}
