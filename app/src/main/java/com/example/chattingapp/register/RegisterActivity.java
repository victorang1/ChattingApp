package com.example.chattingapp.register;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ActivityRegisterBinding;
import com.example.chattingapp.login.LoginActivity;
import com.example.chattingapp.model.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityRegisterBinding mBinding;
    private RegisterViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        mViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        initListener();
    }

    private void initListener() {
        mBinding.btnGotoLogin.setOnClickListener(this);
        mBinding.btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == mBinding.btnGotoLogin) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if (view == mBinding.btnRegister) {
            User user = mBinding.getViewModel();
            if(mViewModel.registUser(user)) {
                Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show();
                gotoLoginActivity();
            } else Toast.makeText(this, "Failed to register...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit the application")
                .setTitle("Quit")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void gotoLoginActivity() {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.setViewModel(new User());
    }
}
