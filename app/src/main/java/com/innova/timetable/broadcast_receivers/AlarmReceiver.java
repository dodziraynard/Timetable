package com.innova.timetable.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.innova.timetable.models.Task;

import java.util.List;

import static com.innova.timetable.utils.DummyData.getDummyTasks;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Boot completed.", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: Boot completed");

        List<Task> tasks = getDummyTasks(); // Get undone tasks.
        for (Task task : tasks) {
            // TODO: set alarm
        }
    }
}
