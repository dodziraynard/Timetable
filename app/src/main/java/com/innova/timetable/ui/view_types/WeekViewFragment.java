package com.innova.timetable.ui.view_types;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.innova.timetable.adapter.WeekLessonAdapter;
import com.innova.timetable.databinding.FragmentWeekViewBinding;
import com.innova.timetable.models.Lesson;
import com.innova.timetable.ui.MainActivityViewModel;

import java.util.List;

public class WeekViewFragment extends Fragment {


    private WeekLessonAdapter mAdapter;
    private List<Lesson> mLessons;
    private MainActivityViewModel mViewModel;

    public WeekViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentWeekViewBinding binding = FragmentWeekViewBinding.inflate(inflater, container, false);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        mAdapter = new WeekLessonAdapter(getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAdapter);

        mViewModel.getLessons().observe(getViewLifecycleOwner(), lessons -> {
            if (lessons.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                return;
            }
            mLessons = lessons;
            mAdapter.setData(mLessons,
                    (int) lessons.get(0).getStartTime() / 60,
                    (int) lessons.get(lessons.size() - 1).getEndTime() / 60);
        });
        return binding.getRoot();
    }
}