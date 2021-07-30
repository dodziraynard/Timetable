package com.innova.timetable.broadcast_receivers;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.innova.timetable.utils.NotificationHelper;

import static com.innova.timetable.utils.Constants.ALARM_RECEIVER_MESSAGE;

public class AlertReceiver extends BroadcastReceiver {
    private static final String TAG = "AlertReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getExtras().getString(ALARM_RECEIVER_MESSAGE);

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(message);
        Notification notification = nb.build();
        notification.flags |= Notification.DEFAULT_SOUND;

        notificationHelper.getManager().notify(1, notification);

        vibrate(context);
    }

    private void vibrate(Context context) {
        long[] pattern = {0, 500, 1000, 300};
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE))
                    .vibrate(VibrationEffect.createWaveform(pattern,
                            VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(pattern, -1);
        }
    }
}
