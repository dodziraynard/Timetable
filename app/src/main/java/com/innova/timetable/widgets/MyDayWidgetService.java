package com.innova.timetable.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.innova.timetable.AppRoomDatabase;
import com.innova.timetable.R;
import com.innova.timetable.models.Lesson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.innova.timetable.AppRoomDatabase.databaseWriteExecutor;
import static com.innova.timetable.utils.Functions.getContrastColor;

public class MyDayWidgetService extends RemoteViewsService {
    private static final String TAG = "MyDayWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyDayWidgetItemFactory(getApplicationContext(), intent);
    }

    public static class MyDayWidgetItemFactory implements RemoteViewsFactory {
        private final Context mContext;
        private int mAppWidgetId;
        List<Lesson> mLessons = new ArrayList<>();
        private final AppRoomDatabase mDatabase;

        MyDayWidgetItemFactory(Context context, Intent intent) {
            this.mContext = context;
            this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            mDatabase = AppRoomDatabase.getDatabase(mContext);

            DateFormat df = new SimpleDateFormat("EEE");
            String today = df.format(Calendar.getInstance().getTime());
            databaseWriteExecutor.execute(() -> {
                mLessons = mDatabase.LessonDao().synGetLessonsOfDay("%" + today.toLowerCase() + "%");
            });
        }

        @Override
        public void onCreate() {
            DateFormat df = new SimpleDateFormat("EEE");
            String today = df.format(Calendar.getInstance().getTime());
            databaseWriteExecutor.execute(() -> {
                mLessons = mDatabase.LessonDao().synGetLessonsOfDay("%" + today.toLowerCase() + "%");
            });
        }

        @Override
        public void onDataSetChanged() {
            DateFormat df = new SimpleDateFormat("EEE");
            String today = df.format(Calendar.getInstance().getTime());
            mLessons = mDatabase.LessonDao().synGetLessonsOfDay("%" + today.toLowerCase() + "%");
        }

        @Override
        public void onDestroy() {
            // Close data source
        }

        @Override
        public int getCount() {
            return mLessons.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_day_lesson);
            views.setTextViewText(R.id.name, mLessons.get(position).getCourseName());
            views.setTextViewText(R.id.duration, mLessons.get(position).describeDuration());
            views.setInt(R.id.relative_layout, "setBackgroundColor", Color.parseColor(mLessons.get(position).getColor()));
            views.setInt(R.id.name, "setTextColor", getContrastColor(Color.parseColor(mLessons.get(position).getColor())));
            views.setInt(R.id.duration, "setTextColor", getContrastColor(Color.parseColor(mLessons.get(position).getColor())));
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
