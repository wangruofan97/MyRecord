package com.example.wang.recorder;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @time: 2020/8/8
 * @author: wang
 * @description:
 */
public class Record {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mDateString;

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setDateString() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        mDateString = dateFormat.format(mDate);
    }

    public String getDateString() {
        return mDateString;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Record() {
        this(UUID.randomUUID());
    }

    public Record(UUID id) {
        mId = id;
        mDate = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        mDateString = dateFormat.format(mDate);
    }
}
