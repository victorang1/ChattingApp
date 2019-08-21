package com.example.chattingapp.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.chattingapp.common.DatabaseManager;
import com.example.chattingapp.model.Friend;
import com.example.chattingapp.model.SESSION;
import com.example.chattingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class UserRepository {

    private static UserRepository instance;
    public static DatabaseReference reference;

    public UserRepository() {
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public static UserRepository getInstance() {
        if(instance == null) {
            return new UserRepository();
        }
        return instance;
    }

    public boolean insertUserToFirebase(User user) {
        try {
            reference.child("user").child(user.getUser_key()).setValue(user);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean insertFriendToFirebase(Friend friend, Friend user) {
        try {
            reference.child("user").child(SESSION.user_key).child("list_friends").child(friend.getFriend_key()).setValue(friend);
            reference.child("user").child(friend.getUser_key()).child("list_friends").child(friend.getFriend_key()).setValue(user);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean removeFriendFromFirebase(Friend friend, String friendKey) {
        try {
            reference.child("user").child(SESSION.user_key).child("list_friends").child(friendKey).removeValue();
            reference.child("user").child(friend.getUser_key()).child("list_friends").child(friendKey).removeValue();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public MutableLiveData<Boolean> checkUserLoginFromFirebase(final User user, final DatabaseManager listener) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        reference.child("user").orderByChild("username").equalTo(user.getUsername())
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

    public void getUserFromFirebase(final User user, final DatabaseManager listener) {
        reference.child("user").orderByChild("username").equalTo(user.getUsername())
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
    }

    public MutableLiveData<List<Friend>> getListFriend(final DatabaseManager listener) {
        final MutableLiveData<List<Friend>> result = new MutableLiveData<>();
        reference.child("user").child(SESSION.user_key).child("list_friends").addValueEventListener(new ValueEventListener() {
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

    public MutableLiveData<Boolean> removeFriendFromUser(Friend friend, final DatabaseManager listener) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        reference.child("user").child(SESSION.user_key).child("list_friends")
                .orderByChild("username").equalTo(friend.getUsername())
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

}
