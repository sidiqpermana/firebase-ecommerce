package com.sidiq.intel.myshoppingmall.base;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Sidiq on 26/07/2016.
 */
public class StoreOnlineApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
