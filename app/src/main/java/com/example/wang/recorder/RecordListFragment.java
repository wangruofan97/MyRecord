package com.example.wang.recorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.util.List;
import java.util.UUID;

import android.app.Activity;

/**
 * @time: 2020/8/10
 * @author: wang
 * @description:
 */
public class RecordListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private RecordAdapter mAdapter;
    private boolean mSubtitleVisible;
    private static int mRecordIndex;

    private static final int REQUEST_CODE_UPDATE = 0;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    /**
     * @time: 2020/8/17
     * @author: wang
     * @description: 建立菜单栏
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_record_list, menu);

        MenuItem subTitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible)
            subTitleItem.setTitle(R.string.hide_subtitle);
        else
            subTitleItem.setTitle(R.string.show_subtitle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * @time: 2020/8/17
     * @author: wang
     * @description: 菜单选择事项处理
     * @param item: 菜单选项
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.new_record:
                Record record = new Record();
                RecordLab.get(getActivity()).addRecord(record);
                Intent intent = RecordPagerActivity.newIntent(getActivity(), record.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @time: 2020/8/17
     * @author: wang
     * @description: 更新记录条数
     */
    private void updateSubtitle() {
        RecordLab recordLab = RecordLab.get(getActivity());
        int recordCount = recordLab.getRecordList().size();
        String subtitle = getString(R.string.subtitle_format, recordCount);

        if (!mSubtitleVisible)
            subtitle = null;

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class RecordHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Record mRecord;

        public RecordHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_record, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.record_title);
            mDateTextView = itemView.findViewById(R.id.record_date);
        }

        public void bind(Record record) {
            mRecord = record;
            mTitleTextView.setText(mRecord.getTitle());
            mDateTextView.setText(mRecord.getDateString());
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(getActivity(),
//                    mRecord.getTitle() + " clicked!", Toast.LENGTH_SHORT)
//                    .show();
            Intent intent = RecordPagerActivity.newIntent(getActivity(), mRecord.getId());
            mRecordIndex = getAdapterPosition();
            startActivityForResult(intent, 0);
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordHolder> {
        private List<Record> mRecords;
        public RecordAdapter(List<Record> records) {
            mRecords = records;
        }

        @Override
        public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecordHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(RecordHolder recordHolder, int position) {
            Record record = mRecords.get(position);
            recordHolder.bind(record);
        }

        @Override
        public int getItemCount() {
            return mRecords.size();
        }

        public void setRecords(List<Record> records) {
            mRecords = records;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        RecordLab recordLab = RecordLab.get(getActivity());
        List<Record> records = recordLab.getRecordList();

        if (mAdapter == null)
        {
            mAdapter = new RecordAdapter(records);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else
        {
//            mAdapter.notifyDataSetChanged();
//            System.out.println(mRecordIndex);
            mAdapter.setRecords(records);
            mAdapter.notifyItemChanged(mRecordIndex);
        }
        updateSubtitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.record_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null)
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        updateUI();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }
}
