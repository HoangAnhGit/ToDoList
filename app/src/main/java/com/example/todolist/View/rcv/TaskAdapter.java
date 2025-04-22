package com.example.todolist.View.rcv;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Task;
import com.example.todolist.R;
import com.example.todolist.Utils.CustomToast;
import com.example.todolist.Utils.TimeUtils;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.ItemTaskBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> implements Filterable {

    private Context context;
    private final LocalDate dateToday = LocalDate.now();
    private  List<Task> mList;
    private  List<Task> mListOld;
    private onClickItem taskItemEventHandler;


    public interface  onClickItem{
        void onClickTask(Task task);

        //void onClickDeleteTask(Task task);
    }
    private TaskViewModel taskViewModel;


    public void setAdapter( Context context,List<Task> mList,TaskViewModel taskViewModel, onClickItem taskItemEventHandler){
        this.context = context;
        this.mList = mList;
        this.mListOld=mList;
        this.taskViewModel = taskViewModel;
        this.taskItemEventHandler = taskItemEventHandler;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new TaskHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {

        Task task = mList.get(position);
        if( task== null){
            return;
        }


        boolean isCompleted = task.getStatus().equals(TaskStatus.COMPLETED);
        holder.binding.iconComplete.setVisibility(isCompleted ? View.VISIBLE : View.GONE);
        holder.binding.iconNotComplete.setVisibility(isCompleted ? View.GONE : View.VISIBLE);

        if (isCompleted) {
            holder.binding.txtTitle.setPaintFlags(holder.binding.txtTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.binding.txtTitle.setPaintFlags(holder.binding.txtTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }


        //onclick
        holder.binding.iconComplete.setOnClickListener(view -> {
            task.setStatus(TaskStatus.PENDING);
            taskViewModel.update(task);
            holder.binding.iconComplete.setVisibility(View.GONE);
            holder.binding.iconNotComplete.setVisibility(View.VISIBLE);
        });

        holder.binding.iconNotComplete.setOnClickListener(view -> {
            if (!task.getDueDate().isAfter(dateToday)) {
                task.setStatus(TaskStatus.COMPLETED);
                taskViewModel.update(task);
                holder.binding.iconComplete.setVisibility(View.VISIBLE);
                holder.binding.iconNotComplete.setVisibility(View.GONE);
            } else {
                CustomToast.showCustomToastPlus(context,"Let's focus today",Gravity.BOTTOM,R.drawable.hi);
            }
        });

        holder.binding.layoutItem.setOnClickListener(view->taskItemEventHandler.onClickTask(task));


        //chờ fix đang bị đè sự kiện
       // holder.binding.layoutBackground.setOnClickListener(view->taskItemEventHandler.onClickDeleteTask(task));


        //set
        holder.binding.iconTask.setImageResource(task.getIdIcon());
        
        holder.binding.txtTime.setText(
                task.getDueTime() != null ? TimeUtils.toLabel( task.getDueTime()) : "Any time"
        );

        holder.binding.layoutItem.setBackgroundColor( Integer.parseInt(task.getColorCode()));
        if(task.getTitle()==null){
            holder.binding.txtTitle.setText(context.getString(R.string.newTask));
        }else {
            holder.binding.txtTitle.setText(task.getTitle());
        }



    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class TaskHolder extends RecyclerView.ViewHolder{
        public ItemTaskBinding binding;
        public TaskHolder(@NonNull  ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public Task getTaskAt(int position) {
        return mList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                List<Task> filteredList;

                if (strSearch.isEmpty()) {
                    filteredList = new ArrayList<>(mListOld);
                } else {
                    filteredList = new ArrayList<>();
                    for (Task task : mListOld) {
                        if (task.getTitle().toLowerCase().contains(strSearch.toLowerCase())) {
                            filteredList.add(task);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mList = (List<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}
