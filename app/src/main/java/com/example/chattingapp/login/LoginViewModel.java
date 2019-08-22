package com.example.chattingapp.login;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.chattingapp.common.DatabaseManager;
import com.example.chattingapp.model.SESSION;
import com.example.chattingapp.model.User;
import com.example.chattingapp.repository.UserRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class LoginViewModel extends ViewModel {

    private UserRepository mRepository;
    private MutableLiveData<Boolean> isSuccessful;

    public LoginViewModel() {
        this.mRepository = UserRepository.getInstance();
    }

    public MutableLiveData<Boolean> getUser(User user) {
        if(isSuccessful == null) {
            isSuccessful = new MutableLiveData<>();
        }
        isSuccessful = loadData(user);
        return isSuccessful;
    }

    public MutableLiveData<Boolean> loadData(final User user) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        mRepository.checkUserLoginFromFirebase(user, new DatabaseManager() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                result.postValue(false);
                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    User data = item.getValue(User.class);
                    if(user.getPassword().equals(data.getPassword())) {
                        SESSION.username = user.getUsername();
                        SESSION.user_key = item.getKey();
                        result.postValue(true);
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
}
