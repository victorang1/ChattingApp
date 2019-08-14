package com.example.chattingapp.insert;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.example.chattingapp.BR;

public class InsertViewState extends BaseObservable {

    public static final int NO_USER_FOUND = 1;
    public static final int USER_FOUND = 2;
    public static final int SIMILAR_USER_FOUND = 3;

    private int eventId;

    @Bindable
    public int getEventId() {
        return eventId;
    }

    public InsertViewState setEventId(int eventId) {
        this.eventId = eventId;
        notifyPropertyChanged(BR.eventId);
        return this;
    }

    @Bindable
    public int getNoUserContentVisibility() {
        return eventId == NO_USER_FOUND ? View.VISIBLE : View.GONE;
    }
}
