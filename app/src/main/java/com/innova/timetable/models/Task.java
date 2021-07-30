package com.innova.timetable.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static com.innova.timetable.utils.Constants.FIVE_MIN_REMINDER;
import static com.innova.timetable.utils.Constants.NO_REMINDER;


@Entity(tableName = Task.TASK_TABLE)
public class Task implements Parcelable {
    @Ignore
    public static final String TASK_TABLE = "task_table";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private long due;

    @ColumnInfo(name = "is_done")
    private boolean isDone;
    private String reminder;
    private String color;

    public Task(String title, long due, String color) {
        this.title = title;
        this.due = due;
        this.isDone = false;
        this.reminder = NO_REMINDER;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDue() {
        return due;
    }

    public void setDue(long due) {
        this.due = due;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private Task(Parcel in) {
        id = in.readLong();
        title = in.readString();
        due = in.readLong();
        isDone = in.readInt() != 0;
        reminder = in.readString();
        color = in.readString();
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeLong(due);
        parcel.writeInt((isDone ? 1 : 0));
        parcel.writeString(reminder);
        parcel.writeString(color);
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
