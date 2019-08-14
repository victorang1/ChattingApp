package com.example.chattingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.chattingapp.BR;

import java.util.List;

public class User extends BaseObservable {

    private String name;
    private String username;
    private String password;
    private String user_key;
    private List<Friend> friendList;

    @Bindable
    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        return this;
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
        return this;
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
        return this;
    }

    @Bindable
    public String getUser_key() {
        return user_key;
    }

    public User setUser_key(String user_key) {
        this.user_key = user_key;
        notifyPropertyChanged(BR.user_key);
        return this;
    }

    @Bindable
    public List<Friend> getFriendList() {
        return friendList;
    }

    public User setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
        notifyPropertyChanged(BR.friendList);
        return this;
    }
}
