package com.innova.timetable.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.timetable.databinding.ItemWeekLessonBinding;
import com.innova.timetable.models.Lesson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.innova.timetable.utils.Functions.getContrastColor;
import static com.innova.timetable.utils.Functions.getLessonOfHour;

public class WeekLessonAdapter extends RecyclerView.Adapter<WeekLessonAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private final ArrayList<Integer> hours;
    private List<Lesson> lessons;

    private OnItemClickListener listener;

    public WeekLessonAdapter(Context context) {
        mContext = context;
        lessons = new ArrayList<>();
        hours = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Lesson> lessons, int startHour, int endHour) {
        this.lessons = lessons;

        for (int i = startHour; i <= endHour; i++) {
            hours.add(i);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWeekLessonBinding binding = ItemWeekLessonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekLessonAdapter.ViewHolder holder, int position) {
        int hour = hours.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, 0);
        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm aa");
        holder.binding.time.setText(formatTime.format(calendar.getTime()));

        List<Lesson> hourLessons = getLessonOfHour(lessons, hour);
        for (Lesson lesson : hourLessons) {
            displayHourLessons(holder, lesson);
        }
    }

    private void displayHourLessons(WeekLessonAdapter.ViewHolder holder, Lesson lesson) {
        holder.setIsRecyclable(false);

        if (lesson.getDays().contains("mon")) {
            holder.binding.monCard.setBackgroundColor(Color.parseColor(lesson.getColor()));
            holder.binding.monCard.setVisibility(View.VISIBLE);
            holder.binding.monCourse.setText(lesson.getCourseName());
            holder.binding.monVenue.setText(lesson.getVenue());
            holder.binding.monCourse.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
            holder.binding.monVenue.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
        }
        if (lesson.getDays().contains("tue")) {
            holder.binding.tueCard.setBackgroundColor(Color.parseColor(lesson.getColor()));
            holder.binding.tueCard.setVisibility(View.VISIBLE);
            holder.binding.tueCourse.setText(lesson.getCourseName());
            holder.binding.tueVenue.setText(lesson.getVenue());
            holder.binding.tueCourse.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
            holder.binding.tueVenue.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
        }
        if (lesson.getDays().contains("wed")) {
            holder.binding.wedCard.setBackgroundColor(Color.parseColor(lesson.getColor()));
            holder.binding.wedCard.setVisibility(View.VISIBLE);
            holder.binding.wedCourse.setText(lesson.getCourseName());
            holder.binding.wedVenue.setText(lesson.getVenue());
            holder.binding.wedCourse.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
            holder.binding.wedVenue.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
        }
        if (lesson.getDays().contains("thu")) {
            holder.binding.thuCard.setBackgroundColor(Color.parseColor(lesson.getColor()));
            holder.binding.thuCard.setVisibility(View.VISIBLE);
            holder.binding.thuCourse.setText(lesson.getCourseName());
            holder.binding.thuVenue.setText(lesson.getVenue());
            holder.binding.thuCourse.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
            holder.binding.thuVenue.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
        }
        if (lesson.getDays().contains("fri")) {
            holder.binding.friCard.setBackgroundColor(Color.parseColor(lesson.getColor()));
            holder.binding.friCard.setVisibility(View.VISIBLE);
            holder.binding.friCourse.setText(lesson.getCourseName());
            holder.binding.friVenue.setText(lesson.getVenue());
            holder.binding.friCourse.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
            holder.binding.friVenue.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
        }
        if (lesson.getDays().contains("sat")) {
            holder.binding.satCard.setBackgroundColor(Color.parseColor(lesson.getColor()));
            holder.binding.satCard.setVisibility(View.VISIBLE);
            holder.binding.satCourse.setText(lesson.getCourseName());
            holder.binding.satVenue.setText(lesson.getVenue());
            holder.binding.satCourse.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
            holder.binding.satVenue.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
        }
        if (lesson.getDays().contains("sun")) {
            holder.binding.sunCard.setBackgroundColor(Color.parseColor(lesson.getColor()));
            holder.binding.sunCard.setVisibility(View.VISIBLE);
            holder.binding.sunCourse.setText(lesson.getCourseName());
            holder.binding.sunVenue.setText(lesson.getVenue());
            holder.binding.sunCourse.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
            holder.binding.sunVenue.setTextColor(getContrastColor(Color.parseColor(lesson.getColor())));
        }
    }

    @Override
    public int getItemCount() {
        return hours.size();
    }

    public interface OnItemClickListener {
        void setOnLessonEditListener(Lesson lesson);

        void setOnLessonDeleteListener(Lesson lesson);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemWeekLessonBinding binding;

        public ViewHolder(@NonNull ItemWeekLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//            binding.btnMore.setOnClickListener(view -> {
//                PopupMenu popup = new PopupMenu(view.getContext(), itemView);
//                popup.setOnMenuItemClickListener(item -> {
//                    if (item.getItemId() == R.id.action_edit) {
//                        if (listener != null)
//                            listener.setOnLessonEditListener(lessons.get(getLayoutPosition()));
//                        return true;
//                    } else if (item.getItemId() == R.id.action_delete) {
//                        listener.setOnLessonDeleteListener(lessons.get(getLayoutPosition()));
//                    }
//                    return false;
//                });
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    popup.setGravity(Gravity.END);
//                }
//                popup.inflate(R.menu.lesson_option_menu);
//                popup.show();
//            });
        }
    }
}
