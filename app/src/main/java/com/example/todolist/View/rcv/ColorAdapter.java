package com.example.todolist.View.rcv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    private final List<Integer> colors;
    private final Context context;
    private int selectedPosition = -1;

    private final OnColorClickListener colorClick;

    public interface OnColorClickListener {
        void onColorClick(int color);
    }

    public ColorAdapter(Context context, List<Integer> colors,  OnColorClickListener colorClick) {
        this.context = context;

        this.colors = colors;
        this.colorClick = colorClick;
    }

    @androidx.annotation.NonNull
    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@androidx.annotation.NonNull @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_color, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        int currentPosition = holder.getAdapterPosition();


        if (currentPosition != RecyclerView.NO_POSITION) {
            holder.colorCard.setCardBackgroundColor(ContextCompat.getColor(context, colors.get(currentPosition)));

            holder.itemView.setOnClickListener(v -> {
                if (selectedPosition != currentPosition) {
                    int previousPosition = selectedPosition;
                    selectedPosition = currentPosition;
                    notifyItemChanged(previousPosition);
                    notifyItemChanged(selectedPosition);
                    colorClick.onColorClick(colors.get(currentPosition));
                }
            });



            holder.checkMark.setVisibility(selectedPosition == currentPosition ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public static class ColorViewHolder extends RecyclerView.ViewHolder {
        CardView colorCard;
        ImageView checkMark;

        public ColorViewHolder(View itemView) {
            super(itemView);
            colorCard = itemView.findViewById(R.id.colorCard);
            checkMark = itemView.findViewById(R.id.btnCheck);
        }
    }
}
