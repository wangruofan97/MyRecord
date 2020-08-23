package com.example.wang.recorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.wang.recorder.RecordDbSchema.*;

/**
 * @time: 2020/8/10
 * @author: wang
 * @description:
 */
public class RecordLab {
    private static RecordLab sRecordLab;
//    private List<Record> mRecordList;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static RecordLab get(Context context) {
        if (sRecordLab == null)
            sRecordLab = new RecordLab(context);
        return sRecordLab;
    }

    public void addRecord(Record record) {
        ContentValues values = getContentValues(record);
        mDatabase.insert(RecordTable.NAME, null, values);
//        mRecordList.add(record);
    }

    public void updateRecord(Record record) {
        String uuidString = record.getId().toString();
        ContentValues values = getContentValues(record);
        mDatabase.update(RecordTable.NAME, values,
                RecordTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deleteRecord(Record record) {
//        mRecordList.remove(record);
        String uuidString = record.getId().toString();
        ContentValues values = getContentValues(record);
        mDatabase.delete(RecordTable.NAME, RecordTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    private RecordCursorWrapper queryRecords(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RecordTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new RecordCursorWrapper(cursor);
    }

    private RecordLab(Context context) {
//        mRecordList = new ArrayList<>();
        mContext = context.getApplicationContext();
        mDatabase = new RecordBaseHelper(mContext).getWritableDatabase();
//        for (int i = 0; i < 100; i++) {
//            Record record = new Record();
//            record.setTitle("Crime #" + i);
//            record.setSolved(i % 2 == 0); // Every other one
//            mRecordList.add(record);
//        }
    }

    public List<Record> getRecordList() {
//        return mRecordList;
        List<Record> records = new ArrayList<>();

        RecordCursorWrapper cursor = queryRecords(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                records.add(cursor.getRecord());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return records;
    }

    public Record getRecord(UUID id) {
//        for (Record record: mRecordList)
//            if (record.getId().equals(id))
//                return record;
        RecordCursorWrapper cursor = queryRecords(
                RecordTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );
        try {
            if (cursor.getCount() == 0)
                return null;
            cursor.moveToFirst();
            return cursor.getRecord();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Record record) {
        ContentValues values = new ContentValues();
        values.put(RecordTable.Cols.UUID, record.getId().toString());
        values.put(RecordTable.Cols.TITLE, record.getTitle());
        values.put(RecordTable.Cols.DATE, record.getDate().getTime());
        values.put(RecordTable.Cols.SOLVED, record.isSolved() ? 1 : 0);
        return values;
    }
}
