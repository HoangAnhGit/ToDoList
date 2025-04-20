package com.example.todolist.View.rcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Task;
import com.example.todolist.ViewModel.TaskViewModel;
import com.example.todolist.databinding.ItemTaskBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> implements Filterable {

    Context context;
    private  List<Task> mList;
    private  List<Task> mListOld;
    private onClickItem taskItemEventHandler;


    public interface  onClickItem{
        void onClickTask(Task task);
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

        holder.binding.iconComplete.setOnClickListener(view -> {
            task.setStatus(TaskStatus.PENDING);
            taskViewModel.update(task);
            holder.binding.iconComplete.setVisibility(View.GONE);
            holder.binding.iconNotComplete.setVisibility(View.VISIBLE);
        });

        holder.binding.iconNotComplete.setOnClickListener(view -> {
            task.setStatus(TaskStatus.COMPLETED);
            taskViewModel.update(task);
            holder.binding.iconComplete.setVisibility(View.VISIBLE);
            holder.binding.iconNotComplete.setVisibility(View.GONE);
        });

        holder.binding.iconTask.setImageResource(task.getIdIcon());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        holder.binding.txtTime.setText(
                task.getDueTime() != null ? task.getDueTime().format(formatter) : "Any time"
        );


        holder.binding.layoutItem.setBackgroundColor( Integer.parseInt(task.getColorCode()));
        holder.binding.txtTitle.setText(task.getTitle());

        holder.binding.layoutItem.setOnClickListener(view->taskItemEventHandler.onClickTask(task));
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class TaskHolder extends RecyclerView.ViewHolder{
        ItemTaskBinding binding;
        public TaskHolder(@NonNull  ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
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
