package com.innova.timetable.paging;

import android.content.Context;

import androidx.paging.DataSource;

import com.innova.timetable.models.Lesson;

public class LessonDataSourceFactory extends DataSource.Factory<Integer, Lesson> {
    private Context mContext;
    private LessonDataSource mDataSource;

    public LessonDataSourceFactory(Context context) {
        mContext = context;
    }


    @Override
    public DataSource<Integer, Lesson> create() {
        if (mDataSource == null) {
            mDataSource = new LessonDataSource(mContext);
        }
        return mDataSource;
    }
}
