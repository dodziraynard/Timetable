package com.innova.timetable.utils;

import com.innova.timetable.models.Lesson;
import com.innova.timetable.models.Task;

import java.util.ArrayList;
import java.util.List;

public class DummyData {

    public static List<Lesson> getDummyLessons() {
        List<Lesson> lessons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Lesson lesson = new Lesson("Course " + i, "C" + i, 60 * (i + 7),
                    60 * (i + 7) + 60, "JQB 24", "#" + i + "f0d0" + i, "PK Osei");
            lesson.setDays("mon wed fri");
            lessons.add(lesson);
        }
        return lessons;
    }

    public static List<Task> getDummyTasks() {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Task task = new Task("Solve Cal II Homework " + i, 1611363600764L, "#" + (i + 4) + "f000a");
            task.setId(i);
            if (i % 2 != 0)
                task.setDone(true);
            tasks.add(task);
        }
        return tasks;
    }
}
