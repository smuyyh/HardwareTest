/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ztemt.test.basic.item.nfc.record;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.nfc.NdefRecord;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.ztemt.test.basic.R;

/**
 * A {@link ParsedNdefRecord} corresponding to a MIME object.
 */
public class MimeRecord extends ParsedNdefRecord {
    private final String mType;
    private final byte[] mContent;

    private MimeRecord(String mimeType, byte[] content) {
        mType = Preconditions.checkNotNull(mimeType);
        Preconditions.checkNotNull(content);
        mContent = Arrays.copyOf(content, content.length);
    }

    @VisibleForTesting
    public String getMimeType() {
        return mType;
    }

    @VisibleForTesting
    public byte[] getContent() {
        return Arrays.copyOf(mContent, mContent.length);
    }

    @Override
    public View getView(Activity activity, LayoutInflater inflater, ViewGroup parent, int offset) {
        TextView text = (TextView) inflater.inflate(R.layout.nfc_tag_text, parent, false);
        text.setText(mType);
        return text;
    }

    @Override
    public String getSnippet(Context context, Locale locale) {
        return mType;
    }

    public static MimeRecord parse(NdefRecord record) {
        Preconditions.checkArgument(record.getTnf() == NdefRecord.TNF_MIME_MEDIA);
        String type = new String(record.getType(), Charset.forName("US-ASCII"));
        byte[] payload = record.getPayload();
        return new MimeRecord(type, payload);
    }

    public static boolean isMime(NdefRecord record) {
        try {
            parse(record);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static NdefRecord newMimeRecord(String type, byte[] data) {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(data);

        byte[] typeBytes = type.getBytes(Charset.forName("US-ASCII"));

        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, typeBytes, new byte[0], data);
    }
}
