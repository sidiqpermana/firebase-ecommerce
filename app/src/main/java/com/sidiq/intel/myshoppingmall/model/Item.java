package com.sidiq.intel.myshoppingmall.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sidiq on 26/07/2016.
 */
public class Item implements Parcelable {
    private String product_id;
    private String name;
    private String image;
    private int price;

    public Item(){

    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeInt(this.price);
    }

    protected Item(Parcel in) {
        this.product_id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.price = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
