package com.innova.timetable.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.innova.timetable.R;
import com.innova.timetable.adapter.DayLessonPagedAdapter;
import com.innova.timetable.databinding.ActivityMainBinding;
import com.innova.timetable.paging.MainActivityViewModelFactory;
import com.innova.timetable.ui.tasks.TasksActivity;
import com.innova.timetable.ui.view_types.DayViewFragment;
import com.innova.timetable.ui.view_types.WeekViewFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "HRD";
    private FragmentTransaction mFragmentTransaction;
    private ActivityMainBinding mBinding;
    private FragmentManager mFragmentManager;
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        mFragmentTransaction.replace(mBinding.frameLayout.getId(), new DayViewFragment());
        mFragmentTransaction.commit();

        mBinding.fab.setOnClickListener(view -> {
            startActivity(new Intent(this, NewLessonActivity.class));
        });

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.updateWidget();

        // Displaying banner ads
        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mBinding.adView.loadAd(adRequest);

        // View model with paging
        DayLessonPagedAdapter adapter = new DayLessonPagedAdapter();
        // Set adapter on Recylerview.
        MainActivityViewModel activityViewModel =
                ViewModelProviders.of(this,
                        new MainActivityViewModelFactory(this.getApplication())
                ).get(MainActivityViewModel.class);
        activityViewModel.getPagedLessons().observe(this, lessons -> {
            adapter.submitList(lessons);
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_week_view) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.replace(mBinding.frameLayout.getId(), new WeekViewFragment());
            mFragmentTransaction.commit();
            mBinding.fab.setVisibility(View.GONE);
        } else if (item.getItemId() == R.id.action_day_view) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.replace(mBinding.frameLayout.getId(), new DayViewFragment());
            mFragmentTransaction.commit();
            mBinding.fab.setVisibility(View.VISIBLE);
        } else if (item.getItemId() == R.id.action_tasks) {
            startActivity(new Intent(this, TasksActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}