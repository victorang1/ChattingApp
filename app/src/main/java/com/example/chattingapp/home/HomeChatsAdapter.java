package com.example.chattingapp.home;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.HomeItemChatsLayoutBinding;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.Friend;

import java.util.List;

public class HomeChatsAdapter extends RecyclerView.Adapter<HomeChatsAdapter.MyViewHolder> {

    private List<Chat> chatList;
    private OnItemClickListener listener;

    public HomeChatsAdapter(List<Chat> chatList, OnItemClickListener listener) {
        this.chatList = chatList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private HomeItemChatsLayoutBinding itemBinding;

        public MyViewHolder(@NonNull HomeItemChatsLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    @NonNull
    @Override
    public HomeChatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        HomeItemChatsLayoutBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.home_item_chats_layout, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeChatsAdapter.MyViewHolder holder, int position) {
        final Chat chat = chatList.get(position);
        String marker = chat.getTime().substring(chat.getTime().length()-2);
        String time = chat.getTime().substring(0, 5);
        chat.setTime(time + " " + marker);
        holder.itemBinding.setViewModel(chat);
        holder.itemBinding.setDate(chat);
        holder.itemBinding.cvChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface OnItemClickListener {
        void onClick(Chat chat);
    }
}
