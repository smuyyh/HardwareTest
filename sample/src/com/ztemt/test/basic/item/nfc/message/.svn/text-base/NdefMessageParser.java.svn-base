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

package com.ztemt.test.basic.item.nfc.message;

import java.util.ArrayList;
import java.util.List;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import com.ztemt.test.basic.item.nfc.record.ImageRecord;
import com.ztemt.test.basic.item.nfc.record.MimeRecord;
import com.ztemt.test.basic.item.nfc.record.ParsedNdefRecord;
import com.ztemt.test.basic.item.nfc.record.TextRecord;
import com.ztemt.test.basic.item.nfc.record.UnknownRecord;
import com.ztemt.test.basic.item.nfc.record.UriRecord;

/**
 * Utility class for creating {@link ParsedNdefMessage}s.
 */
public class NdefMessageParser {

    // Utility class
    private NdefMessageParser() { }

    /** Parse an NdefMessage */
    public static ParsedNdefMessage parse(NdefMessage message) {
        return new ParsedNdefMessage(getRecords(message));
    }

    public static List<ParsedNdefRecord> getRecords(NdefMessage message) {
        return getRecords(message.getRecords());
    }

    public static List<ParsedNdefRecord> getRecords(NdefRecord[] records) {
        List<ParsedNdefRecord> elements = new ArrayList<ParsedNdefRecord>();
        for (NdefRecord record : records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record));
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record));
            } else if (ImageRecord.isImage(record)) {
                elements.add(ImageRecord.parse(record));
            } else if (MimeRecord.isMime(record)) {
                elements.add(MimeRecord.parse(record));
            } else {
                elements.add(new UnknownRecord());
            }
        }
        return elements;
    }
}
