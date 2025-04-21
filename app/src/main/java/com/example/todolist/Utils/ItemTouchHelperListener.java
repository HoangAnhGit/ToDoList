package com.example.todolist.Utils;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperListener {
    void onSwipe(RecyclerView.ViewHolder viewHolder);
}
