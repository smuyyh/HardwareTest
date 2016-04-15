package com.ztemt.test.basic.item;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztemt.test.basic.R;

public class ReceiverTest extends SpeakerTest {

    private AudioManager mAudioManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.receiver, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAudioManager.setMode(AudioManager.MODE_IN_CALL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.receiver_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("receiver", true);
    }
}
