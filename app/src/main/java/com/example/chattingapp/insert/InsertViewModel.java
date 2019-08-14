package com.example.chattingapp.insert;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.chattingapp.common.DatabaseManager;
import com.example.chattingapp.model.Chat;
import com.example.chattingapp.model.Friend;
import com.example.chattingapp.model.SESSION;
import com.example.chattingapp.model.User;
import com.example.chattingapp.repository.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class InsertViewModel extends ViewModel {

    private UserRepository mRepository;
    private InsertViewState mState;
    private MutableLiveData<User> mUser;

    public InsertViewModel() {
        mRepository = UserRepository.getInstance();
        mState = new InsertViewState();
    }

    public MutableLiveData<User> getUser(User user) {
        if(mUser == null) {
            mUser = new MutableLiveData<>();
        }
        mUser = loadUser(user);
        return mUser;
    }

    private MutableLiveData<User> loadUser(final User user) {
        final MutableLiveData<User> result = new MutableLiveData<>();
        if(user.getUsername().equals(SESSION.username)) {
            result.postValue(null);
            mState.setEventId(InsertViewState.NO_USER_FOUND);
        } else {
            mRepository.getUserFromFirebase(user, new DatabaseManager() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    mState.setEventId(InsertViewState.NO_USER_FOUND);
                    result.postValue(null);
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        User data = item.getValue(User.class);
                        if (user.getUsername().equals(data.getUsername())) {
                            if(checkDuplicateFriendList(data)) {
                                result.postValue(data);
                                mState.setEventId(InsertViewState.SIMILAR_USER_FOUND);
                            } else {
                                result.postValue(data);
                                mState.setEventId(InsertViewState.USER_FOUND);
                            }
                        }
                        break;
                    }
                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    result.postValue(null);
                }
            });
        }
        return result;
    }

    private Boolean checkDuplicateFriendList(User user) {
        if(!SESSION.friendList.isEmpty()) {
            for(Friend data : SESSION.friendList) {
                if(user.getUsername().equals(data.getUsername())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean insertFriendToDatabase(User data) {
        String key = UserRepository.reference.child("user").push().getKey();
        Friend friend = new Friend(data.getName(), data.getUsername(), data.getUser_key(), new ArrayList<Chat>());
        Friend user = new Friend(SESSION.username,  SESSION.username, SESSION.user_key, new ArrayList<Chat>());
        if(mRepository.insertFriendToFirebase(friend, user, key)) {
            return true;
        }
        return false;
    }

    public int getEventId() {
        return mState.getEventId();
    }
}
