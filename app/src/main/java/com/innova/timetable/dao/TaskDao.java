package com.innova.timetable.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.innova.timetable.models.Task;

import java.util.List;

import static com.innova.timetable.models.Task.TASK_TABLE;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTask(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTasks(List<Task> tasks);

    @Query("DELETE FROM " + TASK_TABLE)
    void deleteAll();

    @Delete
    void deleteTask(Task task);

    @Delete
    void deleteTasks(List<Task> tasks);

    @Query("SELECT * from " + TASK_TABLE + " ORDER BY is_done DESC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM " + TASK_TABLE + " WHERE id = :id")
    LiveData<Task> getTask(long id);
}
