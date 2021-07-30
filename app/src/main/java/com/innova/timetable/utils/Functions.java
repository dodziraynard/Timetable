package com.innova.timetable.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.graphics.ColorUtils;

import com.innova.timetable.models.Lesson;
import com.innova.timetable.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Functions {
    private static final String TAG = "Functions";

    public static ArrayList<View> findViewsByTag(ViewGroup viewGroup, String tag) {
        ArrayList<View> views = new ArrayList<>();

        // Get the number of views on the layout
        final int childCount = viewGroup.getChildCount();

        // Iterate over the views found in the layout
        for (int i = 0; i < childCount; i++) {
            final View view = viewGroup.getChildAt(i);

            // If the view found in contains over views
            if (view instanceof ViewGroup) {
                // Recursively call the findViewsByTag function on the viewGroup
                views.addAll(findViewsByTag((ViewGroup) view, tag));
            }
            final Object tagObject = view.getTag();
            if (tagObject != null && tagObject.equals(tag)) {
                views.add(view);
            }
        }
        return views;
    }

    public static List<Lesson> getLessonOfHour(List<Lesson> lessons, int hour) {
        List<Lesson> hourLessons = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson.getStartTime() / 60 >= hour && lesson.getStartTime() / 60 < hour + 1)
                hourLessons.add(lesson);
        }
        return hourLessons;
    }

    public static int getContrastColor(int color) {
        double contrast = ColorUtils.calculateContrast(color, Color.WHITE);
        if (contrast > 1.5) {
            return Color.WHITE;
        }
        return Color.BLACK;
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void setOrUpdateAlarm(Context context, Task task, long mBeforeReminderInMills) {
        if (mBeforeReminderInMills != -1L) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.setTimeInMillis(task.getDue());
            calendar.add(Calendar.MILLISECOND, (int) -mBeforeReminderInMills);

            AlarmHelper.startAlarm(context, task.getTitle(), calendar, (int) task.getId());
            long diff = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            hours = hours > 0 ? hours : 0;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
            minutes = minutes > 0 ? minutes : 0;

            Toast.makeText(context, String.format("Next alarm in %s hours, %s minutes", hours, minutes), Toast.LENGTH_SHORT).show();
        }
    }

    public static void setOrUpdateWeeklyAlarm(Context context, Lesson lesson, long mBeforeReminderInMills) {
        if (mBeforeReminderInMills != -1L) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.AM_PM, Calendar.AM);
            calendar.set(Calendar.HOUR, (int) (lesson.getStartTime() / 60));
            calendar.set(Calendar.MINUTE, (int) (lesson.getStartTime() % 60));
            calendar.add(Calendar.MILLISECOND, (int) -mBeforeReminderInMills);

            for (String day : lesson.getDays().split(" ")) {
                switch (day.toLowerCase()) {
                    case "mon":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                        AlarmHelper.startWeeklyAlarm(context, lesson.getCourseName(), calendar, (int) lesson.getId());
                        break;
                    case "tue":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                        AlarmHelper.startWeeklyAlarm(context, lesson.getCourseName(), calendar, (int) lesson.getId());
                        break;
                    case "wed":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                        AlarmHelper.startWeeklyAlarm(context, lesson.getCourseName(), calendar, (int) lesson.getId());
                        break;
                    case "thu":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                        AlarmHelper.startWeeklyAlarm(context, lesson.getCourseName(), calendar, (int) lesson.getId());
                        break;
                    case "fri":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                        AlarmHelper.startWeeklyAlarm(context, lesson.getCourseName(), calendar, (int) lesson.getId());
                        break;
                    case "sat":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        AlarmHelper.startWeeklyAlarm(context, lesson.getCourseName(), calendar, (int) lesson.getId());
                        break;
                    case "sun":
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        AlarmHelper.startWeeklyAlarm(context, lesson.getCourseName(), calendar, (int) lesson.getId());
                        break;
                }
            }

            Toast.makeText(context, "Alarms are set for: " + lesson.getDays(), Toast.LENGTH_SHORT).show();
        }
    }
}
