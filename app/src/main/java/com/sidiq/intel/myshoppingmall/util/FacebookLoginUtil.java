package com.sidiq.intel.myshoppingmall.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class FacebookLoginUtil {
    public static final String TAG = FacebookLoginUtil.class.getSimpleName();
    private LoginButton fbLoginButton;
    private CallbackManager callbackManager;
    private Activity mActivity;
    private OnFacebookLoginSuccessListener onFacebookLoginSuccessListener;

    public FacebookLoginUtil(LoginButton fbLoginButton, Activity activity) {
        this.fbLoginButton = fbLoginButton;
        this.mActivity = activity;

        callbackManager = CallbackManager.Factory.create();
        if (fbLoginButton != null){
            this.fbLoginButton.setReadPermissions("email", "public_profile");
        }
    }

    public OnFacebookLoginSuccessListener getOnFacebookLoginSuccessListener() {
        return onFacebookLoginSuccessListener;
    }

    public void setOnFacebookLoginSuccessListener(OnFacebookLoginSuccessListener onFacebookLoginSuccessListener) {
        this.onFacebookLoginSuccessListener = onFacebookLoginSuccessListener;
    }

    public void doLogin() {
        fbLoginButton.setReadPermissions("public_profile","email");
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onLoginFacebookSuccessd");
                getOnFacebookLoginSuccessListener().onFacebookLoginSuccess(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        fbLoginButton.performClick();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnFacebookLoginSuccessListener{
        void onFacebookLoginSuccess(LoginResult loginResult);
    }
}


