package com.example.chattingapp.home;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.chat.ChatActivity;
import com.example.chattingapp.databinding.ActivityHomeBinding;
import com.example.chattingapp.databinding.HomeCustomFriendsDialogBinding;
import com.example.chattingapp.insert.InsertActivity;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.Friend;
import com.example.chattingapp.model.SESSION;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityHomeBinding mBinding;
    private HomeViewModel mViewModel;
    private HomeFriendsAdapter friendsAdapter;
    private HomeChatsAdapter chatsAdapter;
    private List<Friend> friendList = new ArrayList<>();
    private List<Friend> chatFriendList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            setVisibility(item.getItemId());
            switch (item.getItemId()) {
                case R.id.navigation_friends:
                    setupFriendsTabView();
                    return true;
                case R.id.navigation_chats:
                    setupChatsTabView();
                    return true;
                case R.id.navigation_profile:
                    setupProfileTabView();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initListener();
        setupFriendsTabView();
    }

    private void setupFriendsTabView() {
        mBinding.tvMenuToolbar.setText(R.string.title_friends);
        mViewModel.getFriendList().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(@Nullable List<Friend> friends) {
                friendList.clear();
                friendList.addAll(friends);
                friendsAdapter.notifyDataSetChanged();
            }
        });
        initFriendsAdapter();
    }

    private void initFriendsAdapter() {
        HomeFriendsAdapter.OnItemClickListener listener = new HomeFriendsAdapter.OnItemClickListener() {
            @Override
            public void onClick(Friend friend) {
                showFriendDialog(friend);
            }
        };
        friendsAdapter = new HomeFriendsAdapter(friendList, listener);
        mBinding.recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerViewFriends.setHasFixedSize(true);
        mBinding.recyclerViewFriends.setAdapter(friendsAdapter);
    }

    private void showFriendDialog(final Friend friend) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final HomeCustomFriendsDialogBinding dialogBinding = HomeCustomFriendsDialogBinding
                .inflate(LayoutInflater.from(this), (ViewGroup) mBinding.getRoot(), false);
        dialogBinding.setViewModel(friend);
        builder.setView(dialogBinding.getRoot());
        final AlertDialog dialog = builder.create();
        dialogBinding.llChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("value", friend);
                gotoChatActivity(bundle);
            }
        });
        dialogBinding.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.removeFriend(friend).observe(HomeActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(HomeActivity.this, "Delete success", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(HomeActivity.this, "Failed to delete friend", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    private void setupChatsTabView() {
        mBinding.tvMenuToolbar.setText(R.string.title_chats);
        mViewModel.loadChatList();
        mViewModel.getChatList().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(@Nullable List<Friend> friends) {
                chatFriendList.clear();
                chatFriendList.addAll(friends);
                chatsAdapter.notifyDataSetChanged();
            }
        });
        initChatsFriendAdapter();
    }

    private void initChatsFriendAdapter() {
        chatsAdapter = new HomeChatsAdapter(chatFriendList);
        mBinding.recyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerViewFriends.setHasFixedSize(true);
        mBinding.recyclerViewFriends.setAdapter(chatsAdapter);
    }

    private void setupProfileTabView() {
        mBinding.tvMenuToolbar.setText(R.string.title_profile);
    }

    private void setVisibility(int resId) {
        if(resId == R.id.navigation_friends) {
            mBinding.llFriendsContainer.setVisibility(View.VISIBLE);
            mBinding.llChatsContainer.setVisibility(View.GONE);
            mBinding.llProfileContainer.setVisibility(View.GONE);
        }
        else if(resId == R.id.navigation_chats) {
            mBinding.llFriendsContainer.setVisibility(View.GONE);
            mBinding.llChatsContainer.setVisibility(View.VISIBLE);
            mBinding.llProfileContainer.setVisibility(View.GONE);
        }
        else if(resId == R.id.ll_profile_container) {
            mBinding.llFriendsContainer.setVisibility(View.GONE);
            mBinding.llChatsContainer.setVisibility(View.GONE);
            mBinding.llProfileContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit the application")
                .setTitle("Quit")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initListener() {
        mBinding.fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.fab) {
            gotoInsertActivity();
        }
    }

    private void gotoInsertActivity() {
        finish();
        startActivity(new Intent(this, InsertActivity.class));
    }

    private void gotoChatActivity(Bundle value) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtras(value);
        startActivity(intent);
        finish();
    }
}
