package com.example.todolist.Calendar;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolist.databinding.ItemMonthBinding;

import java.util.List;

public class MothAdapter extends RecyclerView.Adapter<MothAdapter.MonthHolder> {


    private static final List<CallMonth> mList = CallMonth.monthsInYear();
    @NonNull
    @Override
    public MonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMonthBinding binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MonthHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthHolder holder, int position) {
        CallMonth month = mList.get(position);
        holder.binding.month.setText(month.getMonthName());

    }

    public CallMonth getItemAtPosition(int position) {
        if (position >= 0 && position < mList.size()) {
            return mList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class MonthHolder extends RecyclerView.ViewHolder {
        ItemMonthBinding binding;
        public MonthHolder(@NonNull  ItemMonthBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
