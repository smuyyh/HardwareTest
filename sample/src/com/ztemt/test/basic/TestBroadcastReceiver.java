package com.ztemt.test.basic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TestBroadcastReceiver extends BroadcastReceiver {

    private static final String SECRET_CODE_ACTION =
        "android.provider.Telephony.SECRET_CODE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, TestListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
