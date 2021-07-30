package com.innova.timetable.ui.tasks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.innova.timetable.AppRoomDatabase;
import com.innova.timetable.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.innova.timetable.AppRoomDatabase.databaseWriteExecutor;

public class TaskActivityViewModel extends AndroidViewModel {
    private static final String TAG = "TaskActivityViewModel";
    private final AppRoomDatabase db;

    public TaskActivityViewModel(@NonNull Application application) {
        super(application);
        db = AppRoomDatabase.getDatabase(application);
    }

    public LiveData<List<Task>> getTasks() {
        return db.TaskDao().getAllTasks();
    }

    public void insertTask(Task task) {
        databaseWriteExecutor.execute(() -> {
            db.TaskDao().insertTask(task);
        });
    }

    public void deleteTask(Task task) {
        databaseWriteExecutor.execute(() -> {
            db.TaskDao().deleteTask(task);
        });
    }

    public void deleteTasks(Map<Integer, Task> tasks) {
        databaseWriteExecutor.execute(() -> {
            db.TaskDao().deleteTasks(new ArrayList<>(tasks.values()));
        });
    }
}
