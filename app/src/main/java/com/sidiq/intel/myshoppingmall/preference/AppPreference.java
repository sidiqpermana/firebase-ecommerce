package com.sidiq.intel.myshoppingmall.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by inte on 7/20/2016.
 */
public class AppPreference {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String PREFS_NAME = "MyShoppingAppPrefs";
    private String KEY_EMAIL = "EMAIL";
    private String KEY_USERID = "USERID";
    private String KEY_USERNAME = "username";

    public AppPreference(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public void setUserId(String userId){
        editor.putString(KEY_USERID, userId);
        editor.commit();
    }

    public String getUserId(){
        return sharedPreferences.getString(KEY_USERID, "");
    }

    public void setUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername(){
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
