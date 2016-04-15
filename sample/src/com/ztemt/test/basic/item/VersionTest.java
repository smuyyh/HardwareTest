package com.ztemt.test.basic.item;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class VersionTest extends BaseTest {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.version, container, false);
        setVersionInfo(v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.version_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("version", true);
    }

    private void setVersionInfo(View v) {
        TextView codename = (TextView) v.findViewById(R.id.version_software_codename);
        TextView incremental = (TextView) v.findViewById(R.id.version_software_incremental);
        TextView release = (TextView) v.findViewById(R.id.version_software_release);
        TextView sdk = (TextView) v.findViewById(R.id.version_software_sdk);
        TextView baseband = (TextView) v.findViewById(R.id.version_software_baseband);
        TextView display = (TextView) v.findViewById(R.id.version_software_display);

        codename.setText(Build.VERSION.CODENAME);
        incremental.setText(Build.VERSION.INCREMENTAL);
        release.setText(Build.VERSION.RELEASE);
        sdk.setText(Build.VERSION.SDK_INT + "");
        baseband.setText(Build.getRadioVersion());
        display.setText(Build.DISPLAY);

        TextView board = (TextView) v.findViewById(R.id.version_hardware_board);
        TextView model = (TextView) v.findViewById(R.id.version_hardware_model);
        TextView device = (TextView) v.findViewById(R.id.version_hardware_device);
        TextView manufacture = (TextView) v.findViewById(R.id.version_hardware_manufacture);

        board.setText(Build.BOARD);
        model.setText(Build.MODEL);
        device.setText(Build.DEVICE);
        manufacture.setText(Build.MANUFACTURER);
    }
}
