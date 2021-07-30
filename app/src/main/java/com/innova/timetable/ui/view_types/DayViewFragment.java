package com.innova.timetable.ui.view_types;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.innova.timetable.adapter.TabAdapter;
import com.innova.timetable.databinding.FragmentDayViewBinding;
import com.innova.timetable.ui.days.DayFragment;
import com.innova.timetable.utils.Day;

public class DayViewFragment extends Fragment {

    public DayViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDayViewBinding binding = FragmentDayViewBinding.inflate(inflater, container, false);

        TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new DayFragment(Day.MONDAY), "Monday");
        adapter.addFragment(new DayFragment(Day.TUESDAY), "Tuesday");
        adapter.addFragment(new DayFragment(Day.WEDNESDAY), "Wednesday");
        adapter.addFragment(new DayFragment(Day.THURSDAY), "Thursday");
        adapter.addFragment(new DayFragment(Day.FRIDAY), "Friday");
        adapter.addFragment(new DayFragment(Day.SATURDAY), "Saturday");
        adapter.addFragment(new DayFragment(Day.SUNDAY), "Sunday");

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        return binding.getRoot();
    }
}