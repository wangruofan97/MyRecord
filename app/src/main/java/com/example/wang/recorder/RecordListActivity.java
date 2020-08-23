package com.example.wang.recorder;

import android.support.v4.app.Fragment;

/**
 * @time: 2020/8/10
 * @author: wang
 * @description:
 */
public class RecordListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RecordListFragment();
    }
}
