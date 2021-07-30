package com.innova.timetable;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.innova.timetable.dao.LessonDao;
import com.innova.timetable.dao.TaskDao;
import com.innova.timetable.models.Lesson;
import com.innova.timetable.models.Task;

import java.util.List;

public class DataRepository {
    public static final String TAG = "DataRepository";
    private final LiveData<List<Lesson>> mLessons;
    private final LiveData<List<Task>> mTasks;

    Application mApplication;
    private final LessonDao mLessonDao;
    private final TaskDao mTaskDao;

    public DataRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mApplication = application;

        mLessonDao = db.LessonDao();
        mTaskDao = db.TaskDao();

        mLessons = mLessonDao.getLessonsOfDay("mon");
        mTasks = mTaskDao.getAllTasks();
    }

    public LiveData<List<Lesson>> getAllLessons() {
        return mLessons;
    }

    public long insertLesson(Lesson lesson) {
        return mLessonDao.insertLesson(lesson);
    }
}
