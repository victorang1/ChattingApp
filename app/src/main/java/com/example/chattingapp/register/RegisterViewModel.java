package com.example.chattingapp.register;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.chattingapp.model.User;
import com.example.chattingapp.repository.UserRepository;

public class RegisterViewModel extends ViewModel {

    private UserRepository mRepository;

    public RegisterViewModel() {
        this.mRepository = UserRepository.getInstance();
    }

    public boolean registUser(User user) {
        user.setUser_key(UserRepository.reference.child("user").push().getKey());
        if(mRepository.insertUserToFirebase(user)) {
            return true;
        }
        return false;
    }
}
