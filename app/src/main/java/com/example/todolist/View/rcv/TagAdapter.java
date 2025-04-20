package com.example.todolist.View.rcv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.Tag;
import com.example.todolist.R;
import com.example.todolist.databinding.ItemTagBinding;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private  List<Tag> mList ;
    private itemSelected onClick;
    private Tag selectedTag;
    private String selectedColorCode;

    public interface itemSelected{
        void OnClickItem(Tag tag);
    }

    public void setListTag(  List<Tag> mList,Tag selectedTag, String selectedColorCode,itemSelected onClick){
        this.mList = mList;
        this.selectedTag=selectedTag;
        this.selectedColorCode=selectedColorCode;
        this.onClick= onClick;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTagBinding binding = ItemTagBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new TagViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        Tag tag = mList.get(position);
        holder.binding.title.setText(tag.getTitle());

        boolean isSelected = selectedTag != null && tag.getUid() == selectedTag.getUid();

        if (isSelected) {
            holder.binding.item.setBackgroundColor(Integer.parseInt(selectedColorCode));
            holder.binding.Picked.setVisibility(View.VISIBLE);
        } else {
            holder.binding.item.setBackgroundResource(R.color.white);
            holder.binding.Picked.setVisibility(View.GONE);
        }

        holder.binding.getRoot().setOnClickListener(v -> {
            selectedTag = tag;
            onClick.OnClickItem(tag);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public static class TagViewHolder extends RecyclerView.ViewHolder {
        ItemTagBinding binding;
        public TagViewHolder(@NonNull ItemTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
