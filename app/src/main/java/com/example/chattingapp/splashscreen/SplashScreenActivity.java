package com.example.chattingapp.splashscreen;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chattingapp.R;
import com.example.chattingapp.common.SessionManager;
import com.example.chattingapp.databinding.ActivitySplashScreenBinding;
import com.example.chattingapp.home.HomeActivity;
import com.example.chattingapp.login.LoginActivity;
import com.example.chattingapp.model.SESSION;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    private ActivitySplashScreenBinding mBinding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        session = new SessionManager(getApplicationContext());
        setSplashScreen();
    }

    private void setSplashScreen() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(checkSession()) {
                    intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                } else {
                    intent =  new Intent(SplashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },2000);
    }

    private boolean checkSession() {
        if(session.checkLogin()) {
            HashMap<String, String> user = session.getUserDetails();
            SESSION.username = user.get(SessionManager.KEY_USERNAME);
            SESSION.user_key = user.get(SessionManager.KEY_USERKEY);
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
