package com.example.chattingapp.insert;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ActivityInsertBinding;
import com.example.chattingapp.home.HomeActivity;
import com.example.chattingapp.model.User;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityInsertBinding mBinding;
    private InsertViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_insert);
        mViewModel = ViewModelProviders.of(this).get(InsertViewModel.class);
        initListener();
        mBinding.setViewModel(new User());
    }

    private void initListener() {
        mBinding.btnSearch.setOnClickListener(this);
        mBinding.btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.btnSearch) {
            setLoading(true);
            mBinding.llContainer.setVisibility(View.VISIBLE);
            User user = mBinding.getViewModel();
            mViewModel.getUser(user).observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    setLoading(false);
                    manageEventId(user);
                }
            });
        }
        else if(view == mBinding.btnBack) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void manageEventId(final User data) {
        if(mViewModel.getEventId() == InsertViewState.NO_USER_FOUND) {
            setupUserNoFoundVisibility();
            Toast.makeText(this, "No user found", Toast.LENGTH_SHORT).show();
        }
        else {
            mBinding.setViewModel(data);
            if(mViewModel.getEventId() == InsertViewState.SIMILAR_USER_FOUND) {
                setupSimilarUserFoundVisibility();
            } else if (mViewModel.getEventId() == InsertViewState.USER_FOUND) {
                setupUserFoundVisibility();
                mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mViewModel.insertFriendToDatabase(data)) {
                            Toast.makeText(InsertActivity.this, "Add friend success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InsertActivity.this, HomeActivity.class));
                            finish();
                        }
                    }
                });
            }
        }
    }

    private void setupUserNoFoundVisibility() {
        mBinding.rlNoUser.setVisibility(View.VISIBLE);
        mBinding.rlUserFound.setVisibility(View.GONE);
        mBinding.rlSimilarUser.setVisibility(View.GONE);
    }

    private void setupSimilarUserFoundVisibility() {
        mBinding.rlNoUser.setVisibility(View.GONE);
        mBinding.rlUserFound.setVisibility(View.GONE);
        mBinding.rlSimilarUser.setVisibility(View.VISIBLE);
    }

    private void setupUserFoundVisibility() {
        mBinding.rlNoUser.setVisibility(View.GONE);
        mBinding.rlUserFound.setVisibility(View.VISIBLE);
        mBinding.rlSimilarUser.setVisibility(View.GONE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setLoading(boolean isLoading) {
        if(isLoading) {
            mBinding.flLoading.setVisibility(View.VISIBLE);
        }
        else mBinding.flLoading.setVisibility(View.GONE);
    }
}
