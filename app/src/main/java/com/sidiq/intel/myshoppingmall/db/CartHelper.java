package com.sidiq.intel.myshoppingmall.db;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by inte on 7/22/2016.
 */
public class CartHelper {
    private Realm realm;
    private Context mContext;
    public CartHelper(Context mContext){
        this.mContext = mContext;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(mContext).build();
        realm = Realm.getInstance(realmConfiguration);
    }

    public void create(int productId,
                       String productName, String productImage,
                       int productQty, double productPrice){
        realm.beginTransaction();
        CartItem mCartItem = realm.createObject(CartItem.class);
        mCartItem.setId(productId);
        mCartItem.setName(productName);
        mCartItem.setImage(productImage);
        mCartItem.setPrice(productPrice);
        mCartItem.setQty(productQty);
        realm.commitTransaction();
    }

    public ArrayList<CartItem> getAll(){
        ArrayList<CartItem> list = null;
        RealmResults<CartItem> results = realm.where(CartItem.class).findAll();
        if (results.size() > 0){
            list = new ArrayList<>();
            for (CartItem item : results){
                list.add(item);
            }
        }
        return list;
    }

    public void update(int productQty, int productId){
        realm.beginTransaction();
        CartItem mCartItem = realm.where(CartItem.class).equalTo("id", productId).findFirst();
        mCartItem.setQty(productQty);
        realm.commitTransaction();
    }

    public void delete(int productId){
        realm.beginTransaction();
        RealmResults<CartItem> items = realm.where(CartItem.class).equalTo("id", productId).findAll();
        CartItem item = items.get(0);
        item.deleteFromRealm();
        realm.commitTransaction();
    }

    public void clear(){
        realm.beginTransaction();
        RealmResults<CartItem> items = realm.where(CartItem.class).findAll();
        items.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public boolean isItemAlreadyExist(int productId){
        CartItem cartItem = realm.where(CartItem.class)
                .equalTo("id", productId).findFirst();
        if (cartItem != null){return true;}else{return false;}
    }
}
