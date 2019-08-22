package com.example.chattingapp.chat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import com.example.chattingapp.common.DatabaseManager;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.Friend;
import com.example.chattingapp.repository.ChatRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private ChatRepository mRepository;
    private LiveData<List<Chat>> list;
    private DateFormat df;
    private DateFormat tf;

    public ChatViewModel() {
        mRepository = ChatRepository.getInstance();
        tf = new SimpleDateFormat("h:mm:ss:SS a");
        df = new SimpleDateFormat("dd/MM/yy");
    }

    public Friend getResult(Bundle bundle) {
        return (Friend) bundle.getSerializable("value");
    }

    public boolean insertChat(Chat chat, Friend friend) {
        chat.setDate(df.format(Calendar.getInstance().getTime()));
        chat.setTime(tf.format(Calendar.getInstance().getTime()));
        if(mRepository.insertChatToDatabase(chat, friend)) {
            return true;
        }
        return false;
    }

    public LiveData<List<Chat>> getListChat(Bundle bundle) {
        if(list == null) {
            list = new MutableLiveData<>();
        }
        list = loadListChat(bundle);
        return list;
    }

    private MutableLiveData<List<Chat>> loadListChat(Bundle bundle) {
        Friend friend = getResult(bundle);
        final MutableLiveData<List<Chat>> result = new MutableLiveData<>();
        mRepository.loadListChatFromDatabase(friend, new DatabaseManager() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                result.postValue(new ArrayList<Chat>());
                List<Chat> list = new ArrayList<>();
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Chat chat = item.getValue(Chat.class);
                    list.add(chat);
                }
                result.postValue(list);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                result.postValue(new ArrayList<Chat>());
            }
        });
        return result;
    }
}
