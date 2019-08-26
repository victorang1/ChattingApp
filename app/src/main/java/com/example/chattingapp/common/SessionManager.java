package com.example.chattingapp.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.chattingapp.login.LoginActivity;

import java.util.HashMap;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "ChattingApp";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_USERKEY = "user_key";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String username, String user_key) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USERKEY, user_key);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_USERKEY, pref.getString(KEY_USERKEY, null));
        return user;
    }

    public boolean checkLogin() {
        if(this.isloggedIn()) {
            return true;
        }
        return false;
    }

    public boolean isloggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean logoutUser(){
        if(this.isloggedIn()) {
            editor.clear();
            editor.commit();
            return true;
        }
        return false;
    }
}
