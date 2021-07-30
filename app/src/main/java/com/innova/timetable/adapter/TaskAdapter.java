package com.innova.timetable.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.innova.timetable.R;
import com.innova.timetable.databinding.ItemTaskBinding;
import com.innova.timetable.models.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.innova.timetable.utils.Functions.getContrastColor;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private final String TAG = "TaskAdapter";
    private final Context mContext;
    private List<Task> mTasks;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void setOnTaskEditListener(Task task);

        void setOnTaskIsDoneChangeListener(Boolean isChecked, int position);

        void setOnTaskDeleteListener(Task task);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public TaskAdapter(Context context) {
        mContext = context;
        mTasks = new ArrayList<>();
    }

    public void setData(List<Task> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = mTasks.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(task.getDue());
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy hh:mm aa");
        String date = df.format(calendar.getTime());

        holder.binding.title.setText(task.getTitle());
        holder.binding.layout.setBackgroundColor(Color.parseColor(task.getColor()));
        holder.binding.title.setTextColor(getContrastColor(Color.parseColor(task.getColor())));
        holder.binding.due.setTextColor(getContrastColor(Color.parseColor(task.getColor())));
        holder.binding.due.setText(date);
        holder.binding.btnMore.setColorFilter(getContrastColor(Color.parseColor(task.getColor())));

        holder.binding.checkDone.setOnCheckedChangeListener(null);
        holder.binding.checkDone.setChecked(task.isDone());
        holder.binding.checkDone.setOnCheckedChangeListener(holder.onCheckedChanged);

        int[][] states = {{android.R.attr.state_checked}, {}};
        int[] colors = {mContext.getResources().getColor(R.color.orange_800), getContrastColor(Color.parseColor(task.getColor()))};
        CompoundButtonCompat.setButtonTintList(holder.binding.checkDone, new ColorStateList(states, colors));

        if (task.isDone())
            holder.binding.title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        else holder.binding.title.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemTaskBinding binding;

        public ViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnMore.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(view.getContext(), itemView);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.action_edit) {
                        if (mListener != null)
                            mListener.setOnTaskEditListener(mTasks.get(getLayoutPosition()));
                        return true;
                    } else if (item.getItemId() == R.id.action_delete) {
                        mListener.setOnTaskDeleteListener(mTasks.get(getLayoutPosition()));
                    }
                    return false;
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popup.setGravity(Gravity.END);
                }
                popup.inflate(R.menu.task_option_menu);
                popup.show();
            });
        }

        public final CompoundButton.OnCheckedChangeListener onCheckedChanged = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (mListener != null)
                    mListener.setOnTaskIsDoneChangeListener(isChecked, getLayoutPosition());
            }
        };
    }
}
