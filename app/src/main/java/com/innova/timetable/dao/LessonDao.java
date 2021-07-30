package com.innova.timetable.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.innova.timetable.models.Lesson;

import java.util.List;

import static com.innova.timetable.models.Lesson.LESSON_TABLE;

@Dao
public interface LessonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertLesson(Lesson task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertLessons(List<Lesson> tasks);

    @Query("DELETE FROM " + LESSON_TABLE)
    void deleteAll();

    @Delete
    void deleteLesson(Lesson task);

    @Query("SELECT * from " + LESSON_TABLE + " ORDER BY start_time ASC")
    LiveData<List<Lesson>> getLessons();

    @Query("SELECT * from " + LESSON_TABLE + " WHERE id >=:from  ORDER BY start_time ASC LIMIT :to")
    List<Lesson> getPageLessons(int from, int to);

    @Query("SELECT * from " + LESSON_TABLE + " WHERE days LIKE :day ORDER BY start_time ASC")
    LiveData<List<Lesson>> getLessonsOfDay(String day);

    @Query("SELECT * from " + LESSON_TABLE + " WHERE days LIKE :day ORDER BY start_time ASC")
    List<Lesson> synGetLessonsOfDay(String day);

    @Query("SELECT * FROM " + LESSON_TABLE + " WHERE id = :id")
    LiveData<Lesson> getLesson(long id);
}
