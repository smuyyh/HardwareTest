package com.ztemt.test.basic.item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztemt.test.basic.R;

public class StorageTest extends SpeakerTest {

    private static final String MUSIC_FILE = "soundtest.mp3";
    private static final int MSG_DELAY_TIME = 3000;

    private File mFile = new File(Environment.getExternalStorageDirectory(), MUSIC_FILE);

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                setStepText(R.string.storage_check_play);
                setTimerTask(MSG_END, MSG_DELAY_TIME);
                playMusic();
            } else if (Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action)
                    || Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
                    || Intent.ACTION_MEDIA_REMOVED.equals(action)) {
                setStepText(R.string.storage_plug_in);
                cancelTimerTask();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.storage, container, false);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addDataScheme("file");
        getActivity().registerReceiver(mReceiver, filter);

        setTimerTask(MSG_START, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Delete music file in storage
        mFile.delete();
    }

    @Override
    public void onHandleMessage(final int index) {
        switch (index) {
        case MSG_START:
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                mReceiver.onReceive(getActivity(), intent);
            }
            setButtonVisibility(false);
            break;
        case MSG_END:
            super.onHandleMessage(index);
            break;
        }
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.storage_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("storage", true);
    }

    private void setStepText(int resId) {
        ((TextView) getView().findViewById(R.id.storage_text)).setText(resId);
    }

    private void playMusic() {
        // Copy the file to storage
        InputStream is = getResources().openRawResource(R.raw.soundtest);
        OutputStream os = null;

        final int length = 1024;
        byte[] buffer = new byte[length];
        int size;

        try {
            os = new FileOutputStream(mFile);
            while ((size = is.read(buffer, 0, length)) != -1) {
                os.write(buffer, 0, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Play music file in storage
        playMusic(mFile);
    }
}
