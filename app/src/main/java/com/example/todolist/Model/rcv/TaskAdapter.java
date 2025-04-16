package com.example.todolist.Model.rcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.Enum.TaskItemEventHandler;
import com.example.todolist.Model.Enum.TaskStatus;
import com.example.todolist.Model.Task;
import com.example.todolist.databinding.ItemTaskBinding;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder>{

    Context context;
    private List<Task> mList;
    TaskItemEventHandler taskItemEventHandler;


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

        if(task.getStatus().equals(TaskStatus.COMPLETED)){
            holder.binding.iconComplete.setVisibility(View.VISIBLE);
            holder.binding.iconNotComplete.setVisibility(View.GONE);
        }
        holder.binding.iconTask.setImageResource(task.getIdIcon());
        holder.binding.txtTime.setText("s");
        holder.binding.txtTitle.setText(task.getTitle());
    }

    @Override
    public int getItemCount() {
        if(!mList.isEmpty()){
            return mList.size();
        }
        return 0;
    }

    public static class TaskHolder extends RecyclerView.ViewHolder{
        ItemTaskBinding binding;
        public TaskHolder(@NonNull  ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public TaskAdapter(Context context, List<Task> mList) {
        this.context = context;
        this.mList = mList;
    }
}
