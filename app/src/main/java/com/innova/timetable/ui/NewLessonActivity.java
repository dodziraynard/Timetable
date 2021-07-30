package com.innova.timetable.ui;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.innova.timetable.R;
import com.innova.timetable.databinding.ActivityNewLessonBinding;
import com.innova.timetable.models.Lesson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.innova.timetable.utils.Constants.FIFTEEN_MIN_REMINDER;
import static com.innova.timetable.utils.Constants.FIVE_MIN_REMINDER;
import static com.innova.timetable.utils.Constants.NO_REMINDER;
import static com.innova.timetable.utils.Constants.SELECTED_LESSON;
import static com.innova.timetable.utils.Constants.THIRTY_MIN_REMINDER;
import static com.innova.timetable.utils.Functions.findViewsByTag;
import static com.innova.timetable.utils.Functions.isNumeric;
import static com.innova.timetable.utils.Functions.setOrUpdateWeeklyAlarm;

public class NewLessonActivity extends AppCompatActivity {
    private static final String TAG = "NewLessonActivity";

    private String mSelectedColor;
    private long mStartTime = 6L * 60;
    private long mEndTime = 7L * 60;
    private ActivityNewLessonBinding mBinding;
    private Lesson mLesson;
    private long mBeforeReminderInMills;
    private List<String> mDurations;
    private String mLessonTimeReminder;
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNewLessonBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
        setTitle(getString(R.string.new_lesson));

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mSelectedColor = "#" + Integer.toHexString(getResources().getColor(R.color.blue_500));
        mBinding.colorView.setOnClickListener(view -> showColorPickerDialog());

        mBinding.startTimeLayout.setOnClickListener(view -> showTimePickerDialog(view, 6, 0));
        mBinding.endTimeLayout.setOnClickListener(view -> showTimePickerDialog(view, 7, 0));

        mBinding.floatingActionButton.setOnClickListener(view -> saveLesson());

        setupReminderSelection();

        // Editing a lesson
        mLesson = getIntent().getParcelableExtra(SELECTED_LESSON);
        if (mLesson != null) {
            mLessonTimeReminder = mLesson.getReminder();
            displayLessonInfo(mLesson);
        }
    }

    private void setupReminderSelection() {
        mDurations = new ArrayList<>();
        mDurations.add(NO_REMINDER);
        mDurations.add(FIVE_MIN_REMINDER);
        mDurations.add(FIFTEEN_MIN_REMINDER);
        mDurations.add(THIRTY_MIN_REMINDER);

        mBeforeReminderInMills = -1L;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mDurations);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinner.setAdapter(dataAdapter);

        mBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                mLessonTimeReminder = mDurations.get(position);
                if (isNumeric(item.split(" ")[0])) {
                    int minutes = Integer.parseInt(item.split(" ")[0]);
                    mBeforeReminderInMills = minutes * 60 * 1000;
                } else mBeforeReminderInMills = -1L;
            }

            @Override
            public void onNothingSelected(AdapterView<?> view) {
            }
        });
    }

    private void displayLessonInfo(Lesson lesson) {
        setTitle(lesson.getCourseName());

        mSelectedColor = lesson.getColor();
        showLessonStartTime((int) (lesson.getStartTime() / 60), (int) lesson.getStartTime() % 60);
        showLessonEndTime((int) (lesson.getEndTime() / 60), (int) lesson.getEndTime() % 60);

        mBinding.courseName.setText(lesson.getCourseName());
        mBinding.courseCode.setText(lesson.getCourseCode());
        mBinding.courseVenue.setText(lesson.getVenue());
        mBinding.lecturer.setText(lesson.getLecturer());

        mBinding.spinner.setSelection(mDurations.indexOf(lesson.getReminder()), true);

        mBinding.colorView.setBackgroundColor(Color.parseColor(lesson.getColor()));

        ArrayList<View> daysCheckboxes = findViewsByTag(mBinding.daysLayout, "days");

        for (View view : daysCheckboxes) {
            CompoundButton checkbox = (CompoundButton) view;
            if (lesson.getDays().contains(checkbox.getText().toString().toLowerCase())) {
                checkbox.setChecked(true);
            }
        }
    }

    private void showColorPickerDialog() {
        ColorPickerDialogBuilder
                .with(this)
                .initialColor(getResources().getColor(R.color.blue_500))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(getString(R.string.ok), (dialog, selectedColor, allColors) -> {
                    mSelectedColor = "#" + Integer.toHexString(selectedColor);
                    mBinding.colorView.setBackgroundColor(Color.parseColor(mSelectedColor));
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                })
                .build()
                .show();
    }

    private void showTimePickerDialog(View view, int hour, int minute) {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewLessonActivity.this,
                R.style.DialogTheme, (timePicker, selectedHour, selectedMinute) -> {

            if (view == mBinding.startTimeLayout)
                showLessonStartTime(selectedHour, selectedMinute);
            else showLessonEndTime(selectedHour, selectedMinute);
        }, hour, minute, false);
        mTimePicker.show();
    }

    private void showLessonStartTime(int hour, int minute) {
        mStartTime = hour * 60 + minute;

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.AM_PM, Calendar.AM);
        startCalendar.set(Calendar.HOUR, hour);
        startCalendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
        SimpleDateFormat formatAmPm = new SimpleDateFormat("a");

        mBinding.startTime.setText(formatTime.format(startCalendar.getTime()));
        mBinding.startAmPm.setText(formatAmPm.format(startCalendar.getTime()));
    }

    private void showLessonEndTime(int hour, int minute) {
        mEndTime = hour * 60 + minute;

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.AM_PM, Calendar.AM);
        startCalendar.set(Calendar.HOUR, hour);
        startCalendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
        SimpleDateFormat formatAmPm = new SimpleDateFormat("a");

        mBinding.endTime.setText(formatTime.format(startCalendar.getTime()));
        mBinding.endAmPm.setText(formatAmPm.format(startCalendar.getTime()));
    }

    private void saveLesson() {
        String courseName = mBinding.courseName.getText().toString();
        String courseCode = mBinding.courseCode.getText().toString();
        String courseVenue = mBinding.courseVenue.getText().toString();
        String lecturer = mBinding.lecturer.getText().toString();

        ArrayList<View> daysCheckboxes = findViewsByTag(mBinding.daysLayout, "days");
        StringBuilder stringBuilder = new StringBuilder();

        for (View view : daysCheckboxes) {
            CompoundButton checkbox = (CompoundButton) view;
            if (checkbox.isChecked()) {
                stringBuilder.append(checkbox.getText().toString().toLowerCase());
                stringBuilder.append(" ");
            }
        }
        String days = stringBuilder.toString().trim();
        if (days.isEmpty()) {
            Toast.makeText(this, "Please choose days.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (courseName.isEmpty()) {
            Toast.makeText(this, "Please enter course name.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mLesson == null) {
            mLesson = new Lesson(courseName, courseCode, mStartTime, mEndTime, courseVenue, mSelectedColor, lecturer);
        } else {
            mLesson.setCourseName(courseName);
            mLesson.setCourseCode(courseCode);
            mLesson.setVenue(courseVenue);
            mLesson.setLecturer(lecturer);
            mLesson.setStartTime(mStartTime);
            mLesson.setEndTime(mEndTime);
            mLesson.setColor(mSelectedColor);
        }
        mLesson.setReminder(mLessonTimeReminder);
        mLesson.setDays(days);

        // Save into the database.
        mViewModel.insertLesson(mLesson);
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();

        // Setup alarm.
        setOrUpdateWeeklyAlarm(this, mLesson, mBeforeReminderInMills);
        onBackPressed();
    }
}