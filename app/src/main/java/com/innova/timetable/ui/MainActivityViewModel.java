package com.innova.timetable.ui;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.innova.timetable.AppRoomDatabase;
import com.innova.timetable.models.Lesson;
import com.innova.timetable.paging.LessonDataSourceFactory;
import com.innova.timetable.widgets.MyDayWidgetProvider;

import java.util.List;

import static com.innova.timetable.AppRoomDatabase.databaseWriteExecutor;

public class MainActivityViewModel extends AndroidViewModel {
    private static final String TAG = "MainActivityViewModel";
    private final AppRoomDatabase db;
    private final LiveData<PagedList<Lesson>> mPagedLessons;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        db = AppRoomDatabase.getDatabase(application);

        // Instantiate LessonDataSourceFactory
        LessonDataSourceFactory factory = new LessonDataSourceFactory(application);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(2)
                .setPageSize(10)
                .build();

        mPagedLessons = new LivePagedListBuilder<>(factory, config).build();
    }

    public LiveData<PagedList<Lesson>> getPagedLessons() {
        return mPagedLessons;
    }

    public LiveData<List<Lesson>> getLessons() {
        return db.LessonDao().getLessons();
    }

    public LiveData<List<Lesson>> getLessonsOfDay(String day) {
        day = "%" + day + "%";
        return db.LessonDao().getLessonsOfDay(day);
    }

    public void insertLesson(Lesson lesson) {
        databaseWriteExecutor.execute(() -> {
            db.LessonDao().insertLesson(lesson);
        });
        updateWidget();
    }

    public void deleteLesson(Lesson lesson) {
        databaseWriteExecutor.execute(() -> {
            db.LessonDao().deleteLesson(lesson);
        });
        updateWidget();
    }

    public void updateWidget() {
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(
                new ComponentName(getApplication(), MyDayWidgetProvider.class));
        MyDayWidgetProvider myWidget = new MyDayWidgetProvider();
        myWidget.onUpdate(getApplication(), AppWidgetManager.getInstance(getApplication()), ids);
    }
}
