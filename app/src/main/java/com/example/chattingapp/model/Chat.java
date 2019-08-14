package com.example.chattingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.chattingapp.BR;

public class Chat extends BaseObservable {

    private String to;
    private String message;
    private String time;

    public Chat() {
    }

    public Chat(String to, String message, String time) {
        this.to = to;
        this.message = message;
        this.time = time;
    }

    @Bindable
    public String getMessage() {
        return message;
    }

    public Chat setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
        return this;
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public Chat setTime(String time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
        return this;
    }

    @Bindable
    public String getTo() {
        return to;
    }

    public Chat setTo(String to) {
        this.to = to;
        notifyPropertyChanged(BR.to);
        return this;
    }
}
