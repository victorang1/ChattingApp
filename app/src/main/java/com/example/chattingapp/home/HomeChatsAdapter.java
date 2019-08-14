package com.example.chattingapp.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattingapp.model.Chat;

import java.util.List;

public class HomeChatsAdapter extends RecyclerView.Adapter<HomeChatsAdapter.MyViewHolder> {

    private List<Chat> chatList;

    public HomeChatsAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public HomeChatsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeChatsAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
