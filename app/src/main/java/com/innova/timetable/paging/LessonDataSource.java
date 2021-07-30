package com.innova.timetable.paging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.innova.timetable.AppRoomDatabase;
import com.innova.timetable.dao.LessonDao;
import com.innova.timetable.models.Lesson;

import java.util.List;

public class LessonDataSource extends PageKeyedDataSource<Integer, Lesson> {
    private LessonDao mLessonDao;

    // You can also pass in the dao directly.
    public LessonDataSource(Context context) {
        mLessonDao = AppRoomDatabase.getDatabase(context).LessonDao();
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Lesson> callback) {
        List<Lesson> lessons = mLessonDao.getPageLessons(0, params.requestedLoadSize);

        //this is required to handle first request after db is created or app is installed
        int noOfTryies = 0;
        while (lessons.size() == 0) {
            lessons = mLessonDao.getPageLessons(0, params.requestedLoadSize);
            noOfTryies++;
            if (noOfTryies == 6) {
                break;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        callback.onResult(lessons, null, lessons.size() + 1);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Lesson> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Lesson> callback) {
        List<Lesson> lessons = mLessonDao.getPageLessons(params.key, params.requestedLoadSize);
        int nextKey = params.key + lessons.size();
        callback.onResult(lessons, nextKey);
    }
}
