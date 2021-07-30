package com.innova.timetable.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.timetable.R;
import com.innova.timetable.databinding.ItemDayLessonBinding;
import com.innova.timetable.models.Lesson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayLessonAdapter extends RecyclerView.Adapter<DayLessonAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private List<Lesson> lessons;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void setOnLessonEditListener(Lesson lesson);

        void setOnLessonDeleteListener(Lesson lesson);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public DayLessonAdapter(Context context) {
        mContext = context;
        lessons = new ArrayList<>();
    }

    public void setData(List<Lesson> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDayLessonBinding binding = ItemDayLessonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayLessonAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.binding.courseColor.setBackgroundColor(Color.parseColor(lesson.getColor()));
        holder.binding.course.setText(String.format("%s | %s", lesson.getCourseCode(), lesson.getCourseName()));
        holder.binding.venue.setText(String.format("%s | %s", lesson.getVenue(), lesson.getLecturer()));


        SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm aa");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, (int) lesson.getStartTime() / 60);
        calendar.set(Calendar.MINUTE, (int) lesson.getStartTime() % 60);
        String startTime = formatTime.format(calendar.getTime());

        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.HOUR, (int) lesson.getEndTime() / 60);
        calendar.set(Calendar.MINUTE, (int) lesson.getEndTime() % 60);
        String endTime = formatTime.format(calendar.getTime());

        holder.binding.time.setText(startTime + " to " + endTime);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemDayLessonBinding binding;

        public ViewHolder(@NonNull ItemDayLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnMore.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(view.getContext(), itemView);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_edit) {
                        if (listener != null)
                            listener.setOnLessonEditListener(lessons.get(getLayoutPosition()));
                        return true;
                    } else if (item.getItemId() == R.id.action_delete) {
                        listener.setOnLessonDeleteListener(lessons.get(getLayoutPosition()));
                    }
                    return false;
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popup.setGravity(Gravity.END);
                }
                popup.inflate(R.menu.lesson_option_menu);
                popup.show();
            });
        }
    }
}
