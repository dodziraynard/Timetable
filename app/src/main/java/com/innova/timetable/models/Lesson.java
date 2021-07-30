package com.innova.timetable.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.innova.timetable.utils.Constants.NO_REMINDER;


@Entity(tableName = Lesson.LESSON_TABLE)
public class Lesson implements Parcelable {
    @Ignore
    public static final String LESSON_TABLE = "lesson_table";
    public static final Creator<Lesson> CREATOR = new Creator<Lesson>() {
        public Lesson createFromParcel(Parcel in) {
            return new Lesson(in);
        }

        public Lesson[] newArray(int size) {
            return new Lesson[size];
        }
    };
    private static final String TAG = "Lesson";
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "course_name")
    private String courseName;
    @ColumnInfo(name = "course_code")
    private String courseCode;
    @ColumnInfo(name = "start_time")
    private long startTime;
    @ColumnInfo(name = "end_time")
    private long endTime;
    private String venue;
    private String color;
    private String lecturer;
    private String days;
    private String reminder;

    public Lesson(String courseName, String courseCode, long startTime, long endTime, String venue, String color, String lecturer) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.color = color;
        this.lecturer = lecturer;
        this.reminder = NO_REMINDER;
    }

    private Lesson(Parcel in) {
        id = in.readLong();
        courseName = in.readString();
        courseCode = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        venue = in.readString();
        color = in.readString();
        lecturer = in.readString();
        days = in.readString();
        reminder = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String describeDuration() {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.AM_PM, Calendar.AM);
        startCalendar.set(Calendar.HOUR, (int) startTime / 60);
        startCalendar.set(Calendar.MINUTE, (int) startTime % 60);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.AM_PM, Calendar.AM);
        endCalendar.set(Calendar.HOUR, (int) endTime / 60);
        endCalendar.set(Calendar.MINUTE, (int) endTime % 60);

        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(format.format(startCalendar.getTime()));
        stringBuilder.append(" to ");
        stringBuilder.append(format.format(endCalendar.getTime()));

        return stringBuilder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return courseName;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(courseName);
        parcel.writeString(courseCode);
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
        parcel.writeString(venue);
        parcel.writeString(color);
        parcel.writeString(lecturer);
        parcel.writeString(days);
        parcel.writeString(reminder);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        Lesson lesson = (Lesson) obj;
        return lesson.getId() == getId() &&
                lesson.getEndTime() == getEndTime() &&
                lesson.getDays().equals(getDays());
    }
}
