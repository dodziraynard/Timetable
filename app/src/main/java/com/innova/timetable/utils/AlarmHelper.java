package com.innova.timetable.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.innova.timetable.broadcast_receivers.AlertReceiver;

import java.util.Calendar;

import static com.innova.timetable.utils.Constants.ALARM_RECEIVER_MESSAGE;

public class AlarmHelper {
    private static final String TAG = "AlarmHelper";

    public static void startWeeklyAlarm(Context context, String message, Calendar calendar, int requestCode) {
        // Cancel previous alarms
        AlarmHelper.cancelAlarm(context, -requestCode);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlertReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(ALARM_RECEIVER_MESSAGE, message);
        intent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }

    public static void startAlarm(Context context, String message, Calendar calendar, int requestCode) {
        // Cancel previous alarms
        AlarmHelper.cancelAlarm(context, requestCode);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlertReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString(ALARM_RECEIVER_MESSAGE, message);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void cancelAlarm(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }
}
