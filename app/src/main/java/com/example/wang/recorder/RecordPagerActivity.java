package com.example.wang.recorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * @time: 2020/8/15
 * @author: wang
 * @description:
 */
public class RecordPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Record> mRecords;

    private static final String EXTRA_RECORD_ID = "com.example.wang.recorder.record_id";

    public static Intent newIntent(Context context, UUID recordId) {
        Intent intent = new Intent(context, RecordPagerActivity.class);
        intent.putExtra(EXTRA_RECORD_ID, recordId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_pager);
        UUID recordId = (UUID) getIntent().getSerializableExtra(EXTRA_RECORD_ID);

        mViewPager = findViewById(R.id.activity_record_pager);
        mRecords = RecordLab.get(this).getRecordList();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Record record = mRecords.get(i);
                return RecordFragment.newInstance(record.getId());
            }

            @Override
            public int getCount() {
                return mRecords.size();
            }
        });
        for (int i = 0; i < mRecords.size(); i++)
        {
            if (mRecords.get(i).getId().equals(recordId))
            {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
