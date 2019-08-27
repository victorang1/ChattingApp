package com.example.chattingapp.chat;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ItemChatLeftBinding;
import com.example.chattingapp.databinding.ItemChatRightBinding;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.SESSION;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    private final static int TYPE_CHAT_LEFT = 1;
    private final static int TYPE_CHAT_RIGHT = 2;

    private List<Chat> chatList;

    public ChatAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemChatLeftBinding leftItemBinding;
        private ItemChatRightBinding rightItemBinding;

        public MyViewHolder(ItemChatLeftBinding binding) {
            super(binding.getRoot());
            this.leftItemBinding = binding;
        }

        public MyViewHolder(ItemChatRightBinding binding) {
            super(binding.getRoot());
            this.rightItemBinding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(chatList.get(position).getFrom().equals(SESSION.username)) {
            return TYPE_CHAT_RIGHT;
        }
        else return TYPE_CHAT_LEFT;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_CHAT_LEFT) {
            ItemChatLeftBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.item_chat_left, parent, false);
            return new MyViewHolder(itemBinding);
        }
        else if (viewType == TYPE_CHAT_RIGHT){
            ItemChatRightBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.item_chat_right, parent, false);
            return new MyViewHolder(itemBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        String marker = chat.getTime().substring(chat.getTime().length()-2);
        String time = "";
        if(chat.getTime().indexOf(":") == 2) {
            time = chat.getTime().substring(0, 5);
        } else if(chat.getTime().indexOf(":") == 1) {
            time = chat.getTime().substring(0, 4);
        }
        chat.setTime(time + " " + marker);
        switch (holder.getItemViewType()) {
            case TYPE_CHAT_LEFT:
                holder.leftItemBinding.setViewModel(chat);
                break;
            case TYPE_CHAT_RIGHT:
                holder.rightItemBinding.setViewModel(chat);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
