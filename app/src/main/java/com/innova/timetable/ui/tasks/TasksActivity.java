package com.innova.timetable.ui.tasks;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.innova.timetable.R;
import com.innova.timetable.adapter.TaskAdapter;
import com.innova.timetable.databinding.ActivityTasksBinding;
import com.innova.timetable.models.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.innova.timetable.utils.Constants.SELECTED_TASK;

public class TasksActivity extends AppCompatActivity {
    private static final String TAG = "TasksActivity";

    private TaskAdapter mAdapter;
    private List<Task> mTasks;
    private TaskActivityViewModel mViewModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTasksBinding binding = ActivityTasksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle("Tasks");

        mViewModel = new ViewModelProvider(this).get(TaskActivityViewModel.class);

        mAdapter = new TaskAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
        binding.recyclerView.setAdapter(mAdapter);

        mViewModel.getTasks().observe(this, tasks -> {
            if (tasks.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
                return;
            }
            mTasks = tasks;
            Collections.sort(mTasks, (a, b) -> Boolean.compare(a.isDone(), b.isDone()));
            mAdapter.setData(mTasks);
        });

        mAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void setOnTaskEditListener(Task task) {
                Intent intent = new Intent(TasksActivity.this, NewTaskActivity.class);
                intent.putExtra(SELECTED_TASK, task);
                startActivity(intent);
            }

            @Override
            public void setOnTaskIsDoneChangeListener(Boolean isChecked, int position) {
                mTasks.get(position).setDone(isChecked);
                mAdapter.notifyItemChanged(position);
                mViewModel.insertTask(mTasks.get(position));
            }

            @Override
            public void setOnTaskDeleteListener(Task task) {
                showDeleteSnackBar(task);
            }
        });
    }

    private void showDeleteSnackBar(Task task) {
        int position = mTasks.indexOf(task);
        mTasks.remove(task);
        mAdapter.notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.task_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> {
                    mTasks.add(position, task);
                    mAdapter.notifyItemInserted(position);
                });

        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event != DISMISS_EVENT_ACTION) {
                    mViewModel.deleteTask(task);
                }
                super.onDismissed(transientBottomBar, event);
            }
        });
        snackbar.show();
    }

    public void showNewTaskBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_new_task, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_new_task) {
            startActivity(new Intent(this, NewTaskActivity.class));
        } else if (item.getItemId() == R.id.action_delete) {
            removeSelectedTasks();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeSelectedTasks() {
        if (mTasks == null || mTasks.isEmpty()) return;

        // Keeps track of tasks to be deleted
        Map<Integer, Task> deletedTasks = new HashMap<>();

        for (Iterator<Task> iterator = mTasks.iterator(); iterator.hasNext(); ) {
            Task task = iterator.next();
            if (task.isDone()) {
                int position = mTasks.indexOf(task) + deletedTasks.size();
                deletedTasks.put(position, task);
                iterator.remove();
            }
        }
        mAdapter.notifyDataSetChanged();

        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.task_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> {
                    for (Map.Entry<Integer, Task> entry : deletedTasks.entrySet()) {
                        int position = entry.getKey();
                        Task task = entry.getValue();
                        mTasks.add(position, task);
                    }
                    mAdapter.notifyDataSetChanged();
                });
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if (event != DISMISS_EVENT_ACTION) {
                    mViewModel.deleteTasks(deletedTasks);
                }
                super.onDismissed(transientBottomBar, event);
            }
        });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tasks, menu);
        return true;
    }
}