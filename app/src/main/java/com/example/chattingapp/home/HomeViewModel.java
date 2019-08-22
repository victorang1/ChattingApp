package com.example.chattingapp.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private UserRepository mRepository;
    private ChatRepository chatRepository;
    private MutableLiveData<List<Friend>> friendList;
    private MutableLiveData<List<Chat>> chatList;
    private MutableLiveData<Boolean> isSuccessful;
    private MutableLiveData<Friend> mFriend;

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
                            updateUserSessionFriendList(friend);
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

    public MutableLiveData<List<Chat>> getChatList() {
        if(chatList == null) {
            chatList = new MutableLiveData<>();
        }
        chatList = loadChatList();
        return chatList;
    }

    private MutableLiveData<List<Chat>> loadChatList() {
        final MutableLiveData<List<Chat>> result = new MutableLiveData<>();
        final List<Chat> list = new ArrayList<>();
        if (SESSION.friendList.isEmpty()) {
            result.postValue(new ArrayList<Chat>());
        }
        for(final Friend friend : SESSION.friendList){
            chatRepository.loadSingleLastChatFromDatabase(friend, new DatabaseManager() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    List<Chat> temp = new ArrayList<>();
                    for(DataSnapshot item : dataSnapshot.getChildren()) {
                        Chat chat = item.getValue(Chat.class);
                        temp.add(chat);
                    }
                    if(temp.size() >= 1) {
                        Chat last = temp.get(temp.size()-1);
                        if(SESSION.username.equals(last.getTo())) {
                            String from = last.getFrom();
                            last.setFrom(last.getTo());
                            last.setTo(from);
                        }
                        list.add(last);
                    }
                    result.postValue(list);
                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    result.postValue(null);
                }
            });
        }
        return result;
    }

    private void updateUserSessionFriendList(Friend friend) {
        for(Friend item : SESSION.friendList) {
            if(item.getFriend_key().equals(friend.getFriend_key())) {
                SESSION.friendList.remove(item);
                break;
            }
        }
    }

    public MutableLiveData<Friend> getFriend(Chat chat) {
        if(mFriend == null) {
            mFriend = new MutableLiveData<>();
        }
        mFriend = loadFriend(chat);
        return mFriend;
    }

    private MutableLiveData<Friend> loadFriend(final Chat chat) {
        final MutableLiveData<Friend> result = new MutableLiveData<>();
        mRepository.getFriendFromDatabase(chat, new DatabaseManager() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot item : dataSnapshot.getChildren()) {
                        Friend data = item.getValue(Friend.class);
                        if(data.getUsername().equals(chat.getTo())) {
                            result.postValue(data);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                result.postValue(null);
            }
        });
        return result;
    }
}
