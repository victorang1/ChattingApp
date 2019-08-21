package com.example.chattingapp.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.chattingapp.common.DatabaseManager;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.Friend;
import com.example.chattingapp.model.SESSION;
import com.example.chattingapp.repository.ChatRepository;
import com.example.chattingapp.repository.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private UserRepository mRepository;
    private ChatRepository chatRepository;
    private MutableLiveData<List<Friend>> friendList;
    private MutableLiveData<List<Friend>> chatList;
    private MutableLiveData<Boolean> isSuccessful;

    public HomeViewModel() {
        this.mRepository = UserRepository.getInstance();
        this.chatRepository = ChatRepository.getInstance();
    }

    public MutableLiveData<List<Friend>> getFriendList() {
        if(friendList == null) {
            friendList = new MutableLiveData<>();
        }
        friendList = loadListFriend();
        return friendList;
    }

    private MutableLiveData<List<Friend>> loadListFriend() {
        final MutableLiveData<List<Friend>> result = new MutableLiveData<>();
        mRepository.getListFriend(new DatabaseManager() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<Friend> list = new ArrayList<>();
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Friend friend = item.getValue(Friend.class);
                    list.add(friend);
                }
                SESSION.friendList = list;
                result.postValue(list);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                result.postValue(null);
            }
        });
        return result;
    }

    public MutableLiveData<Boolean> removeFriend(final Friend friend) {
        if(isSuccessful == null) {
            isSuccessful = new MutableLiveData<>();
        }
        isSuccessful = loadFriend(friend);
        return isSuccessful;
    }

    private MutableLiveData<Boolean> loadFriend(final Friend friend) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        mRepository.removeFriendFromUser(friend, new DatabaseManager() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                result.postValue(false);
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Friend data = item.getValue(Friend.class);
                    if(data.getUsername().equals(friend.getUsername())) {
                        if(mRepository.removeFriendFromFirebase(friend, item.getKey())) {
                            result.postValue(true);
                        }
                    }
                    break;
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                result.postValue(false);
            }
        });
        return result;
    }

    public void loadChatList() {
        for(final Friend friend : SESSION.friendList) {
            chatRepository.loadListChatFromDatabase(friend, new DatabaseManager() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    List<Chat> list = new ArrayList<>();
                    for(DataSnapshot item : dataSnapshot.getChildren()) {
                        Chat chat = item.getValue(Chat.class);
                        list.add(chat);
                    }
                    friend.setListChat(list);
                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    databaseError.getMessage();
                }
            });
        }
    }

    public MutableLiveData<List<Friend>> getChatList() {
        if(chatList == null) {
            chatList = new MutableLiveData<>();
        }
        chatList = generateChatList();
        return chatList;
    }

    private MutableLiveData<List<Friend>> generateChatList() {
        final MutableLiveData<List<Friend>> result = new MutableLiveData<>();
        mRepository.getListFriend(new DatabaseManager() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                List<Friend> list = new ArrayList<>();
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Friend friend = item.getValue(Friend.class);
                    list.add(friend);
                }
                result.postValue(list);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                result.postValue(new ArrayList<Friend>());
            }
        });
        return result;
    }
}
