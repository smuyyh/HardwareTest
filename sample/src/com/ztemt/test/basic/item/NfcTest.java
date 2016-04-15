package com.ztemt.test.basic.item;

import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztemt.test.basic.R;
import com.ztemt.test.basic.item.nfc.message.NdefMessageParser;
import com.ztemt.test.basic.item.nfc.record.ParsedNdefRecord;

public class NfcTest extends BaseTest {

    private static final String TAG = "NfcTest";

    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    private NfcAdapter mNfcAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nfc, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        Intent intent = new Intent(getActivity(), getActivity().getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        mTechLists = new String[][] {new String[] {MifareClassic.class.getName()}};
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {ndef};

        setButtonVisibility(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(getActivity(), mPendingIntent,
                    mFilters, mTechLists);
            setStepText(mNfcAdapter.isEnabled() ? R.string.nfc_check_tag
                    : R.string.nfc_turn_on);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(getActivity());
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        resolveIntent(intent);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.nfc_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasNfc = pm.hasSystemFeature(PackageManager.FEATURE_NFC);
        return hasNfc && getSystemProperties("nfc", true);
    }

    private void resolveIntent(Intent intent) {
        // Parse the intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage msg = null;
            if (rawMsgs != null && rawMsgs.length > 0) {
                msg = (NdefMessage) rawMsgs[0];
            }

            buildTagViews(msg);
        } else {
            Log.e(TAG, "Unknown intent " + intent);
        }
    }

    private void buildTagViews(NdefMessage msg) {
        LinearLayout content = (LinearLayout) getView().findViewById(R.id.nfc_tag_list);
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        // Clear out any old views in the content area, for example if you 
        // scan two tags in a row.
        content.removeAllViews();

        // Build views for all of the sub records
        List<ParsedNdefRecord> records;
        if (msg != null && (records = NdefMessageParser.parse(msg).getRecords()).size() > 0) {
            // Parse the first message in the list
            for (int i = 0; i < records.size(); i++) {
                ParsedNdefRecord record = records.get(i);
                content.addView(record.getView(getActivity(), inflater, content, i));
                inflater.inflate(R.layout.nfc_tag_divider, content, true);
            }
        } else {
            TextView empty = (TextView) inflater.inflate(R.layout.nfc_tag_text,
                    content, false);
            empty.setText(R.string.nfc_tag_empty);
            content.addView(empty);
        }
    }

    private void setStepText(int resId) {
        ((TextView) getView().findViewById(R.id.nfc_text)).setText(resId);
    }
}
