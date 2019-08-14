package com.example.chattingapp.home;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.HomeItemFriendsLayoutBinding;
import com.example.chattingapp.model.Friend;

import java.util.List;

public class HomeFriendsAdapter extends RecyclerView.Adapter<HomeFriendsAdapter.MyViewHolder> {

    private List<Friend> friendList;
    private OnItemClickListener listener;

    public HomeFriendsAdapter(List<Friend> friendList, OnItemClickListener listener) {
        this.friendList = friendList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private HomeItemFriendsLayoutBinding itemBinding;

        public MyViewHolder(@NonNull HomeItemFriendsLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }

    @NonNull
    @Override
    public HomeFriendsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        HomeItemFriendsLayoutBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.home_item_friends_layout, parent, false);
        return new MyViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFriendsAdapter.MyViewHolder holder, int position) {
        final Friend friend = friendList.get(position);
        holder.itemBinding.setViewModel(friend);
        holder.itemBinding.cvFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(friend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public interface OnItemClickListener {
        void onClick(Friend friend);
    }
}
