package com.example.todolist.Calendar;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.databinding.ItemDayBinding;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayHolder> {


    private List<CallDay> mList;
    private OnDayClick DayClick;
    public interface OnDayClick{
             void onDayClick();
    }
    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DayHolder extends RecyclerView.ViewHolder {
        ItemDayBinding binding;
        public DayHolder(@NonNull  ItemDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
