package com.sidiq.intel.myshoppingmall.api.response;

/**
 * Created by inte on 7/21/2016.
 */
public class User extends BaseResponse{
    private String userId;
    private String username;
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
