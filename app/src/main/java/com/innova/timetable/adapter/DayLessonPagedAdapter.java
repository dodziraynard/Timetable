package com.innova.timetable.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.timetable.databinding.ItemDayLessonBinding;
import com.innova.timetable.models.Lesson;

/**
 * this adapter displays coupon items in recycler view
 * it extends PagedListAdapter which gets data from PagedList
 * and displays in recycler view as data is available in PagedList
 */
public class DayLessonPagedAdapter extends PagedListAdapter<Lesson, DayLessonPagedAdapter.LessonViewHolder> {

    //DiffUtil is used to find out whether two object in the list are same or not
    public static DiffUtil.ItemCallback<Lesson> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Lesson>() {
                @Override
                public boolean areItemsTheSame(@NonNull Lesson oldLesson,
                                               @NonNull Lesson newLesson) {
                    return oldLesson.getId() == newLesson.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Lesson oldLesson,
                                                  @NonNull Lesson newLesson) {
                    return oldLesson.equals(newLesson);
                }
            };

    public DayLessonPagedAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDayLessonBinding binding = ItemDayLessonBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new LessonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = getItem(position);
        if (lesson != null)
            holder.bindTo(lesson);
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        ItemDayLessonBinding mBinding;

        public LessonViewHolder(ItemDayLessonBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindTo(Lesson lesson) {
            // Update views
            mBinding.venue.setText(lesson.getVenue());
        }
    }
}
