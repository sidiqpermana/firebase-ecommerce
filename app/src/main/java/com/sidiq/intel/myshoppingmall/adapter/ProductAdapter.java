package com.sidiq.intel.myshoppingmall.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sidiq.intel.myshoppingmall.R;
import com.sidiq.intel.myshoppingmall.model.Item;
import com.sidiq.intel.myshoppingmall.util.Util;

import java.util.ArrayList;

/**
 * Created by inte on 7/19/2016.
 */
public class ProductAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Item> listItem;

    public ProductAdapter(Activity activity){
        this.activity = activity;
    }

    public ArrayList<Item> getListItem() {
        return listItem;
    }

    public void setListItem(ArrayList<Item> listItem) {
        this.listItem = listItem;
    }

    @Override
    public int getCount() {
        return getListItem().size();
    }

    @Override
    public Object getItem(int i) {
        return getListItem().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder holder = null;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_product, null);
            holder = new ViewHolder();
            holder.imgItem = (ImageView)convertView.findViewById(R.id.img_item_product);
            holder.tvName = (TextView)convertView.findViewById(R.id.tv_item_name);
            holder.tvPrice = (TextView)convertView.findViewById(R.id.tv_item_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        Item item = getListItem().get(i);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(Util.getCurrency(item.getPrice()));

        Glide.with(activity)
                .load(item.getImage())
                .into(holder.imgItem);

        return convertView;
    }

    static class ViewHolder{
        ImageView imgItem;
        TextView tvName, tvPrice;
    }
}
