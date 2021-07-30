package com.innova.timetable.ui.days;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.innova.timetable.adapter.DayLessonAdapter;
import com.innova.timetable.databinding.FragmentDayBinding;
import com.innova.timetable.models.Lesson;
import com.innova.timetable.ui.MainActivityViewModel;
import com.innova.timetable.ui.NewLessonActivity;
import com.innova.timetable.utils.Day;

import java.util.List;

import static com.innova.timetable.utils.Constants.SELECTED_LESSON;


public class DayFragment extends Fragment {
    public static final String STATE_DAY = "day";
    private String day;
    private List<Lesson> mLessons;
    private DayLessonAdapter mAdapter;
    private MainActivityViewModel mViewModel;


    public DayFragment() {
        // Required empty public constructor
    }

    public DayFragment(Day day) {
        this.day = day.toString().substring(0, 3).toLowerCase();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDayBinding binding = FragmentDayBinding.inflate(inflater,
                container, false);

        // Restoring saved state.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_DAY)) {
            this.day = savedInstanceState.getString(STATE_DAY);
        }

        mAdapter = new DayLessonAdapter(getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(mAdapter);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.getLessonsOfDay(this.day)
                .observe(getViewLifecycleOwner(), lessons -> {
                    if (lessons.isEmpty()) {
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                        return;
                    }
                    mLessons = lessons;
                    mAdapter.setData(mLessons);
                });

        mAdapter.setOnItemClickListener(new DayLessonAdapter.OnItemClickListener() {
            @Override
            public void setOnLessonEditListener(Lesson lesson) {
                Intent intent = new Intent(getActivity(), NewLessonActivity.class);
                intent.putExtra(SELECTED_LESSON, lesson);
                getActivity().startActivity(intent);
            }

            @Override
            public void setOnLessonDeleteListener(Lesson lesson) {
                showDeleteSnackBar(lesson);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_DAY, this.day);
        super.onSaveInstanceState(outState);
    }

    private void showDeleteSnackBar(Lesson lesson) {
        int position = mLessons.indexOf(lesson);
        mLessons.remove(lesson);
        mAdapter.notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                "LESSON DELETED",
                Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> {
                    mLessons.add(position, lesson);
                    mAdapter.notifyItemInserted(position);
                });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event != DISMISS_EVENT_ACTION) {
                    mViewModel.deleteLesson(lesson);
                }
                super.onDismissed(transientBottomBar, event);
            }
        });

        snackbar.show();
    }
}