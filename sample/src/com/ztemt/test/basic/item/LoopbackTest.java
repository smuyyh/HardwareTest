package com.ztemt.test.basic.item;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztemt.test.basic.R;

public class LoopbackTest extends ReceiverTest {

    private static final String TAG = "LoopbackTest";
    private static final int MSG_DELAY_TIME = 10000;

    private Loopback mLoopback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.loopback, container, false);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLoopback();
    }

    @Override
    public void onHandleMessage(final int index) {
        switch (index) {
        case MSG_START:
            setTimerTask(MSG_END, MSG_DELAY_TIME);
            setButtonVisibility(false);
            startLoopback();
            break;
        case MSG_END:
            setButtonVisibility(true);
            stopLoopback();
            break;
        }
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.loopback_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("loopback", true);
    }

    public void startLoopback() {
        if (mLoopback == null) {
            try {
                mLoopback = new Loopback();
                mLoopback.start();
            } catch (Exception e) {
                Log.e(TAG, "startLoopback: " + e.getMessage());
            }
        }
    }

    public void stopLoopback() {
        if (mLoopback != null) {
            try {
                mLoopback.stop();
            } catch (Exception e) {
                Log.e(TAG, "stopLoopback: " + e.getMessage());
            }
            mLoopback = null;
        }
    }

    private class Loopback implements Runnable {

        private static final int RATE_IN_HZ = 8000;

        private AudioRecord mAudioRecord;
        private AudioTrack mAudioTrack;
        private Object mLock = new Object();

        private boolean mLoop = true;
        private int mRecordBuffer = 0;
        private int mTrackBuffer = 0;

        public Loopback() {
            mRecordBuffer = AudioRecord.getMinBufferSize(RATE_IN_HZ,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            mTrackBuffer = AudioTrack.getMinBufferSize(RATE_IN_HZ,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    RATE_IN_HZ, AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    mRecordBuffer * 10);
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    RATE_IN_HZ, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, mTrackBuffer,
                    AudioTrack.MODE_STREAM);
        }

        @Override
        public void run() {
            byte[] buffer = new byte[mRecordBuffer];
            int size;

            if (mAudioRecord != null && mAudioTrack != null) {
                mAudioRecord.startRecording();
                mAudioTrack.play();
            } else {
                mLoop = false;
            }

            synchronized (mLock) {
                while (mLoop && (size = mAudioRecord.read(buffer, 0, mRecordBuffer)) != -1) {
                    mAudioTrack.write(buffer, 0, size);
                }
            }
        }

        public void start() {
            new Thread(this).start();
        }

        public void stop() throws IllegalStateException {
            mLoop = false;

            synchronized (mLock) {
                if (mAudioRecord != null) {
                    mAudioRecord.stop();
                    mAudioRecord.release();
                    mAudioRecord = null;
                }

                if (mAudioTrack != null) {
                    mAudioTrack.stop();
                    mAudioTrack.release();
                    mAudioTrack = null;
                }
            }
        }
    }
}
