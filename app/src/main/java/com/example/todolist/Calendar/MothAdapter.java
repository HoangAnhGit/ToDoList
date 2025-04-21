package com.example.todolist.Calendar;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todolist.databinding.ItemMonthBinding;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MothAdapter extends RecyclerView.Adapter<MothAdapter.MonthHolder> {


    public interface OnClickChangeItemMoth{
        void onClickChange(YearMonth month);
    }

    private OnClickChangeItemMoth onClickChangeItemMoth;

    private YearMonth currentYearMonth = YearMonth.now();

    public void setOnClick(OnClickChangeItemMoth onClickChangeItemMoth){
        this.onClickChangeItemMoth = onClickChangeItemMoth;
    }
    @NonNull
    @Override
    public MonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMonthBinding binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MonthHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthHolder holder, int position) {
        String monthName = currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentYearMonth.getYear();

        holder.binding.month.setText(monthName);
        holder.binding.txtYear.setText(String.valueOf(year));

        holder.binding.btnNextDay.setOnClickListener(v -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            notifyItemChanged(position);
            onClickChangeItemMoth.onClickChange(currentYearMonth);
        });

        holder.binding.btnPrevDay.setOnClickListener(v -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            notifyItemChanged(position);
            onClickChangeItemMoth.onClickChange(currentYearMonth);
        });

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public static class MonthHolder extends RecyclerView.ViewHolder {
        ItemMonthBinding binding;
        public MonthHolder(@NonNull  ItemMonthBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
