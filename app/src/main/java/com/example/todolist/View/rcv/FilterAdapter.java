package com.example.todolist.View.rcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder>{

    private List<String> mList ;
    private Context context;
    private int selectedPosition = 0;
    private OnclickFilter filterClick;
    public interface OnclickFilter{
        void onClickFilter(String filter,int selectedPosition);
    }

    public void setData(Context context,List<String> mList,OnclickFilter filterClick){
        this.context = context;
        this.mList = mList;
        this.filterClick=filterClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FilterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tag_filter, parent, false);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterHolder holder, int position) {
        String filter = mList.get(position);
        holder.tvFilter.setText(filter);

        if (position == selectedPosition) {
            holder.tvFilter.setBackgroundResource(R.drawable.bg_filter_seleted);
            holder.tvFilter.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            holder.tvFilter.setBackgroundResource(R.drawable.bg_filter);
            holder.tvFilter.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.tvFilter.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            filterClick.onClickFilter(filter, position);
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class FilterHolder extends RecyclerView.ViewHolder {
        TextView tvFilter;
        public FilterHolder(@NonNull View itemView) {
            super(itemView);
            this.tvFilter =itemView.findViewById(R.id.textFilter);
        }
    }
}
