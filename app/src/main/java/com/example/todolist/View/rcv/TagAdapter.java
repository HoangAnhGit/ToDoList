package com.example.todolist.View.rcv;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Model.Tag;
import com.example.todolist.View.ActivityView.TagActivity;
import com.example.todolist.databinding.ItemTagBinding;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private  List<Tag> mList ;
    private itemSelected onClick;

    public interface itemSelected{
        void OnClickItem(Tag tag);
    }

    public void setListTag(  List<Tag> mList, itemSelected onClick){
        this.mList = mList;
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

        holder.binding.item.setOnClickListener(view -> {onClick.OnClickItem(tag);});
        holder.binding.title.setText(tag.getTitle());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        ItemTagBinding binding;
        public TagViewHolder(@NonNull ItemTagBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
