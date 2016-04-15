package com.ztemt.test.basic.item;

import java.io.File;
import java.io.IOException;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class MicrophoneTest extends BaseTest {

    private static final String TAG = "MicrophoneTest";
    private static final int MSG_START       = 1;
    private static final int MSG_CLOSE_TO    = 2;
    private static final int MSG_AMPLITUDE_1 = 3;
    private static final int MSG_BLOCK       = 4;
    private static final int MSG_AMPLITUDE_2 = 5;
    private static final int MSG_END         = 6;
    private static final int MAX_TIMES       = 5;

    private MediaRecorder mRecorder;

    private File mRecordFile;

    private int mAmplitude1;
    private int mAmplitude2;
    private int mTime = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.microphone, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setTimerTask(MSG_START, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecord();
    }

    @Override
    public void onHandleMessage(final int index) {
        switch (index) {
        case MSG_START:
            setButtonVisibility(false);
            setStepText(R.string.microphone_close_to);
            setTimerTask(MSG_CLOSE_TO, 3000);
            break;
        case MSG_CLOSE_TO:
            mTime = 1;
            startRecord();
            setTimerTask(MSG_AMPLITUDE_1, 0);
            break;
        case MSG_AMPLITUDE_1:
            if (mRecorder != null) {
                mAmplitude1 = mRecorder.getMaxAmplitude();
            }
            if (mTime < MAX_TIMES) {
                mTime++;
                setTimerTask(MSG_AMPLITUDE_1, 1000);
            } else {
                setStepText(R.string.microphone_block);
                setTimerTask(MSG_BLOCK, 3000);
            }
            break;
        case MSG_BLOCK:
            mTime = 1;
            startRecord();
            setTimerTask(MSG_AMPLITUDE_2, 0);
            break;
        case MSG_AMPLITUDE_2:
            if (mRecorder != null) {
                mAmplitude2 = mRecorder.getMaxAmplitude();
            }
            if (mTime < MAX_TIMES) {
                mTime++;
                setTimerTask(MSG_AMPLITUDE_2, 1000);
            } else {
                setResultText(mAmplitude1, mAmplitude2);
                setTimerTask(MSG_END, 0);
                mTime = 1;
            }
            break;
        case MSG_END:
            setButtonVisibility(true);
            stopRecord();
            break;
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        setTimerTask(MSG_START, 0);
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.microphone_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasMicrophone = pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        return hasMicrophone && getSystemProperties("microphone", true);
    }

    private void setStepText(int resId) {
        setStepText(resId, "");
    }

    private void setStepText(int resId, Object... formatArgs) {
        View v = getView();
        v.findViewById(R.id.microphone_result).setVisibility(View.GONE);

        TextView tv = (TextView) v.findViewById(R.id.microphone_text);
        tv.setVisibility(View.VISIBLE);
        tv.setText(getString(resId, formatArgs));
    }

    private void setResultText(int amplitude1, int amplitude2) {
        // calculate difference
        float diff = (amplitude1 == 0) ? 0 : ((float) (amplitude1 - amplitude2) / amplitude1);

        View v = getView();
        v.findViewById(R.id.microphone_text).setVisibility(View.GONE);
        v.findViewById(R.id.microphone_result).setVisibility(View.VISIBLE);

        TextView tv1 = (TextView) v.findViewById(R.id.microphone_amplitude1);
        tv1.setText(getString(R.string.microphone_amp_value, amplitude1));
        tv1.setTextColor(amplitude1 == 0 ? Color.YELLOW : Color.WHITE);

        TextView tv2 = (TextView) v.findViewById(R.id.microphone_amplitude2);
        tv2.setText(getString(R.string.microphone_amp_value, amplitude2));
        tv2.setTextColor(amplitude1 == 0 || diff < 0 ? Color.YELLOW : Color.WHITE);

        TextView tv3 = (TextView) v.findViewById(R.id.microphone_difference);
        tv3.setText(getString(R.string.microphone_diff_value, diff * 100));
        tv3.setTextColor(diff < 0 ? Color.YELLOW : Color.WHITE);

        TextView tv4 = (TextView) v.findViewById(R.id.microphone_suggest);
        if (diff >= 0.8 && amplitude1 >= amplitude2) {
            tv4.setText(R.string.microphone_suggest_pass);
            tv4.setTextColor(Color.GREEN);
        } else {
            tv4.setText(R.string.microphone_suggest_fail);
            tv4.setTextColor(Color.RED);
        }
    }

    private void startRecord() {
        stopRecord();

        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i(TAG, "Storage is not mounted");
            return;
        }

        mRecorder = new MediaRecorder();
        // Create audio output file path
        mRecordFile = new File(Environment.getExternalStorageDirectory(), "sound.3gp");
        // Set the source for the microphone recoding
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // Set output format of recorded sound
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // Set audio encoding format
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // Set recording output file path
        mRecorder.setOutputFile(mRecordFile.getAbsolutePath());

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }

        if (mRecordFile != null) {
            mRecordFile.delete();
        }
    }
}
