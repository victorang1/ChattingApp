package com.example.chattingapp.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.example.chattingapp.BR;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Chat extends BaseObservable {

    private String from;
    private String to;
    private String message;
    private String time;
    private String date;

    public Chat() {

    }

    public Chat(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public Chat(String from, String to, String message, String time, String date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.time = time;
        this.date = date;
    }

    @Bindable
    public String getFrom() {
        return from;
    }

    public Chat setFrom(String from) {
        this.from = from;
        notifyPropertyChanged(BR.from);
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
    public String getDate() {
        return date;
    }

    public Chat setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
        return this;
    }

    @BindingAdapter("last_chat_time")
    public static void loadDate(TextView view, Chat chat) {
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String currDate = df.format(Calendar.getInstance().getTime());
        if(currDate.equals(chat.getDate())) {
            view.setText(chat.getTime());
        }
        else view.setText(chat.getDate());
    }
}
