package com.sidiq.intel.myshoppingmall.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.sidiq.intel.myshoppingmall.util.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sidiq on 26/07/2016.
 */
@IgnoreExtraProperties
public class Order {
    private long order_id;
    private String user_id;
    private String username;
    private String date;
    private List<OrderItem> list;

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<OrderItem> getList() {
        return list;
    }

    public void setList(List<OrderItem> list) {
        this.list = list;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("order_id", getDate());
        result.put("user_id", getUser_id());
        result.put("username", getUsername());
        result.put("date", Util.getCurrentDate());
        result.put("items", getList());

        return result;
    }
}
