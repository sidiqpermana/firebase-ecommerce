package com.sidiq.intel.myshoppingmall.api;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by inte on 7/21/2016.
 */
public class BaseApi implements BaseApiMethods{
    public String BASE_URL = "http://192.168.20.48:8080/index.php/api";

    public String GET_ALL_PRODUCTS = BASE_URL + "/products/all";
    public String GET_DETAIL_PRODUCTS = BASE_URL + "/products/detail";
    public String POST_REGISTER = BASE_URL + "/user/register";
    public String POST_LOGIN = BASE_URL + "/user/login";

    public AsyncHttpClient getHttpClient(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        return httpClient;
    }

    @Override
    public void callApi() {

    }

    @Override
    public void cancelRequest() {

    }
}
