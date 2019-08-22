package com.example.chattingapp.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.chattingapp.common.DatabaseManager;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.Friend;
import com.example.chattingapp.model.SESSION;
import com.example.chattingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {

    private static ChatRepository instance;
    public static DatabaseReference reference;

    public ChatRepository() {
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public static ChatRepository getInstance() {
        if(instance == null) {
            instance = new ChatRepository();
        }
        return instance;
    }

    public boolean insertChatToDatabase(Chat chat, Friend friend) {
        try {
            reference.child("user").child(SESSION.user_key).child("list_friends").child(friend.getFriend_key())
                    .child("list_chats").push().setValue(chat);
            reference.child("user").child(friend.getUser_key()).child("list_friends").child(friend.getFriend_key())
                    .child("list_chats").push().setValue(chat);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public MutableLiveData<Friend> getFriend(Friend friend, final DatabaseManager listener) {
        MutableLiveData<Friend> result = new MutableLiveData<>();
        reference.child("user").child(SESSION.user_key).child("list_friends").orderByChild("username").equalTo(friend.getUsername())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listener.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onFailure(databaseError);
                    }
                });
        return result;
    }

    public MutableLiveData<List<Chat>> loadListChatFromDatabase(Friend friend, final DatabaseManager listener) {
        MutableLiveData<List<Chat>> result = new MutableLiveData<>();
        reference.child("user").child(SESSION.user_key).child("list_friends")
                .child(friend.getFriend_key()).child("list_chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listener.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onFailure(databaseError);
                    }
                });
        return result;
    }

    public MutableLiveData<Chat> loadSingleLastChatFromDatabase(Friend friend, final DatabaseManager listener) {
        MutableLiveData<Chat> result = new MutableLiveData<>();
        reference.child("user").child(SESSION.user_key).child("list_friends")
                .child(friend.getFriend_key()).child("list_chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listener.onSuccess(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onFailure(databaseError);
                    }
                });
        return result;
    }
}
