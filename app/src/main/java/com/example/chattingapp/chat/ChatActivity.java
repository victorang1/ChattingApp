package com.example.chattingapp.chat;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ActivityChatBinding;
import com.example.chattingapp.home.HomeActivity;
import com.example.chattingapp.home.HomeChatsAdapter;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.Friend;
import com.example.chattingapp.model.SESSION;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityChatBinding mBinding;
    private ChatViewModel mViewModel;
    private ChatAdapter mAdapter;
    private List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        initListener();
        setModelFromIntent();
        mViewModel.getListChat(getIntent().getExtras()).observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(@Nullable List<Chat> chats) {
                if(!chats.isEmpty()) {
                    chatList.clear();
                    chatList.addAll(chats);
                    mAdapter.notifyDataSetChanged();
                    mBinding.recyclerView.smoothScrollToPosition(chatList.size()-1);
                }
            }
        });
        initAdapter();
    }

    private void setModelFromIntent() {
        Friend friend = mViewModel.getResult(getIntent().getExtras());
        mBinding.setViewModel(new Chat(SESSION.username, friend.getUsername()));
    }

    private void initAdapter() {
        mAdapter = new ChatAdapter(chatList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
        mBinding.btnBack.setOnClickListener(this);
        mBinding.btnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == mBinding.btnSend) {
            Chat chat = mBinding.getViewModel();
            chat.setMessage(chat.getMessage().trim());
            if(chat.getMessage() != null && !chat.getMessage().equals("")) {
                if(mViewModel.insertChat(chat, mViewModel.getResult(getIntent().getExtras()))) {
                    mBinding.etChatField.setText("");
                }
            }
        }
        else if(view == mBinding.btnBack) {
            gotoHomeActivity();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        gotoHomeActivity();
    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
