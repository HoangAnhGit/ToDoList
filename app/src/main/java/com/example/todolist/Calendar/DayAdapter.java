package com.example.todolist.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.databinding.ItemDayBinding;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayHolder> {

    private List<LocalDate> mList;
    private LocalDate selectedDate = LocalDate.now();
    private final Context context;
    private final OnDayClick onDayClick;

    public interface OnDayClick {
        void onDayClick(LocalDate dateSelected);
    }

    public DayAdapter(Context context,List<LocalDate> list, OnDayClick onDayClick) {
        this.context=context;
        this.mList = list;
        this.onDayClick = onDayClick;
    }

    public void setNewData(List<LocalDate> newList) {
        this.mList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDayBinding binding = ItemDayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DayHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder holder, int position) {
        LocalDate day = mList.get(position);
        holder.binding.day.setText(String.valueOf(day.getDayOfMonth()));

        //thứ
        String dayOfWeek = day.getDayOfWeek()
                .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        holder.binding.DayInWeek.setText(dayOfWeek);

        int colorSelected = ContextCompat.getColor(context, R.color.purple);
        int color = ContextCompat.getColor(context, R.color.white);



        if (day.equals(selectedDate)) {
            holder.binding.day.setTextColor(Color.BLACK);
            holder.binding.itemDay.setCardBackgroundColor(colorSelected);
        } else {
            holder.binding.day.setTextColor(Color.GRAY);
            holder.binding.itemDay.setCardBackgroundColor(color);
        }

        // Click
        holder.binding.itemDay.setOnClickListener(v -> {
            selectedDate = day;
            onDayClick.onDayClick(selectedDate);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class DayHolder extends RecyclerView.ViewHolder {
        ItemDayBinding binding;

        public DayHolder(@NonNull ItemDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
