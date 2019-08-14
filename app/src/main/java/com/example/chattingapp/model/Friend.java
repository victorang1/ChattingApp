package com.example.chattingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.chattingapp.BR;

import java.util.List;

public class Friend extends BaseObservable {

    private String name;
    private String username;
    private String user_key;
    private List<Chat> listChat;

    public Friend() {

    }

    public Friend(String name, String username, String user_key, List<Chat> listChat) {
        this.name = name;
        this.username = username;
        this.user_key = user_key;
        this.listChat = listChat;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public Friend setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        return this;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public Friend setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
        return this;
    }

    @Bindable
    public String getUser_key() {
        return user_key;
    }

    public Friend setUser_key(String user_key) {
        this.user_key = user_key;
        notifyPropertyChanged(BR.user_key);
        return this;
    }

    @Bindable
    public List<Chat> getListChat() {
        return listChat;
    }

    public Friend setListChat(List<Chat> listChat) {
        this.listChat = listChat;
        notifyPropertyChanged(BR.listChat);
        return this;
    }
}
