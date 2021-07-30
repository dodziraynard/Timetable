package com.innova.timetable.ui.tasks;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.innova.timetable.R;
import com.innova.timetable.databinding.ActivityNewTaskBinding;
import com.innova.timetable.models.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.innova.timetable.utils.Constants.FIFTEEN_MIN_REMINDER;
import static com.innova.timetable.utils.Constants.FIVE_MIN_REMINDER;
import static com.innova.timetable.utils.Constants.NO_REMINDER;
import static com.innova.timetable.utils.Constants.SELECTED_TASK;
import static com.innova.timetable.utils.Constants.THIRTY_MIN_REMINDER;
import static com.innova.timetable.utils.Functions.isNumeric;
import static com.innova.timetable.utils.Functions.setOrUpdateAlarm;

public class NewTaskActivity extends AppCompatActivity {
    private static final String TAG = "NewTaskActivity";

    private ActivityNewTaskBinding mBinding;
    private String mSelectedColor;
    private int mCurrentHour;
    private int mCurrentMinute;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;
    private Calendar mCalendar;
    private Task mSelectedTask;
    private long mBeforeReminderInMills = -1L;
    private List<String> mDurations;
    private String mLessonTimeReminder;
    private TaskActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityNewTaskBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
        setTitle(getString(R.string.new_task));

        mViewModel = new ViewModelProvider(this).get(TaskActivityViewModel.class);
        mCalendar = Calendar.getInstance();
        mSelectedColor = "#" + Integer.toHexString(getResources().getColor(R.color.blue_500));

        setupReminderSelection();

        mSelectedTask = getIntent().getParcelableExtra(SELECTED_TASK);
        if (mSelectedTask != null) {
            mLessonTimeReminder = mSelectedTask.getReminder();
            mSelectedColor = mSelectedTask.getColor();
            mCalendar.setTimeInMillis(mSelectedTask.getDue());
            mBinding.colorView.setBackgroundColor(Color.parseColor(mSelectedTask.getColor()));
            mBinding.title.setText(mSelectedTask.getTitle());
            mBinding.spinner.setSelection(mDurations.indexOf(mSelectedTask.getReminder()), true);
        }

        mCurrentHour = mCalendar.get(Calendar.HOUR);
        mCurrentMinute = mCalendar.get(Calendar.MINUTE);
        mCurrentYear = mCalendar.get(Calendar.YEAR);
        mCurrentMonth = mCalendar.get(Calendar.MONTH);
        mCurrentDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        // Show Date
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(mCalendar.getTime());
        mBinding.dueDate.setText(date);

        // Show time
        showSelectedTime(mCurrentHour, mCurrentMinute);

        mBinding.colorView.setOnClickListener(view -> showColorPickerDialog());
        mBinding.dueTimeLayout.setOnClickListener(view -> showTimePickerDialog(mCurrentHour,
                mCurrentMinute));
        mBinding.dueDate.setOnClickListener(view -> showDatePickerDialog(mCurrentYear,
                mCurrentMonth, mCurrentDay));

        mBinding.floatingActionButton.setOnClickListener(view -> saveTask(mCalendar));
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

    private void saveTask(Calendar calendar) {
        String title = mBinding.title.getText().toString();
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter title.", Toast.LENGTH_SHORT).show();
            return;
        }
        long due = calendar.getTimeInMillis();
        if (mSelectedTask == null)
            mSelectedTask = new Task(title, due, mSelectedColor);
        else {
            mSelectedTask.setTitle(title);
            mSelectedTask.setDue(due);
            mSelectedTask.setColor(mSelectedColor);
        }
        mSelectedTask.setReminder(mLessonTimeReminder);

        setOrUpdateAlarm(this, mSelectedTask, mBeforeReminderInMills);

        mViewModel.insertTask(mSelectedTask);
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    private void showDatePickerDialog(int y, int m, int d) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, R.style.DialogTheme, (picker, year, month, day) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, day);

            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
            String date = df.format(mCalendar.getTime());
            mBinding.dueDate.setText(date);
        }, y, m, d);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(int hour, int minute) {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this,
                R.style.DialogTheme, (timePicker, selectedHour, selectedMinute) -> {
            mCalendar.set(Calendar.AM_PM, Calendar.AM);
            mCalendar.set(Calendar.HOUR, selectedHour);
            mCalendar.set(Calendar.MINUTE, selectedMinute);
            showSelectedTime(selectedHour, selectedMinute);
        }, hour, minute, false);
        mTimePicker.show();
    }

    private void showSelectedTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm");
        SimpleDateFormat formatAmPm = new SimpleDateFormat("a");

        mBinding.dueTime.setText(formatTime.format(calendar.getTime()));
        mBinding.dueAmPm.setText(formatAmPm.format(calendar.getTime()));
    }

    private void showColorPickerDialog() {
        ColorPickerDialogBuilder
                .with(this)
                .initialColor(getResources().getColor(R.color.blue_500))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton(getString(R.string.ok), (dialog, selectedColor, allColors) -> {
                    mSelectedColor = "#" + Integer.toHexString(selectedColor);
                    mBinding.colorView.setBackgroundColor(selectedColor);
                })
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                })
                .build()
                .show();
    }
}