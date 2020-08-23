package com.example.wang.recorder;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import static com.example.wang.recorder.RecordDbSchema.*;

/**
 * @time: 2020/8/17
 * @author: wang
 * @description:
 */
public class RecordCursorWrapper extends CursorWrapper {
    public RecordCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Record getRecord() {
        String uuidString = getString(getColumnIndex(RecordTable.Cols.UUID));
        String title = getString(getColumnIndex(RecordTable.Cols.TITLE));
        long date = getLong(getColumnIndex(RecordTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(RecordTable.Cols.SOLVED));

        Record record = new Record(UUID.fromString(uuidString));
        record.setTitle(title);
        record.setSolved(isSolved != 0);
        record.setDate(new Date(date));
        return record;
    }
}
