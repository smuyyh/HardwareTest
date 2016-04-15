package com.ztemt.test.basic.item;

import com.ztemt.test.basic.R;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GSensorTest extends BaseTest {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gsensor, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
        case Configuration.ORIENTATION_PORTRAIT:
            setCenterText(R.string.gsensor_portrait_success);
            setBottomVisibility(false);
            setTimerTask(0, 2000);
            break;
        case Configuration.ORIENTATION_LANDSCAPE:
            setCenterText(R.string.gsensor_landscape_success);
            setBottomText(R.string.gsensor_landscape_next);
            break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onHandleMessage(final int index) {
        clickPassButton();
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.gsensor_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasGSensor = pm.hasSystemFeature(PackageManager.FEATURE_SCREEN_PORTRAIT)
                && pm.hasSystemFeature(PackageManager.FEATURE_SCREEN_LANDSCAPE);
        return hasGSensor && getSystemProperties("gsensor", true);
    }

    private void setCenterText(int resId) {
        ((TextView) getView().findViewById(R.id.gsensor_center)).setText(resId);
    }

    private void setBottomText(int resId) {
        ((TextView) getView().findViewById(R.id.gsensor_bottom)).setText(resId);
    }

    private void setBottomVisibility(boolean visible) {
        getView().findViewById(R.id.gsensor_bottom).setVisibility(
                visible ? View.VISIBLE : View.INVISIBLE);
    }
}
