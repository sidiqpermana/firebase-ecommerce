package com.sidiq.intel.myshoppingmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sidiq.intel.myshoppingmall.R;
import com.sidiq.intel.myshoppingmall.model.Order;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sidiq on 26/07/2016.
 */
public class OrderHistoryAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Order> list;

    public OrderHistoryAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<Order> getList() {
        return list;
    }

    public void setList(ArrayList<Order> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return getList().size();
    }

    @Override
    public Object getItem(int i) {
        return getList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_order_history, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        Order mOrder = (Order)getItem(i);
        holder.tvItemOrderId.setText("Order ID : "+mOrder.getOrder_id());
        holder.tvItemOrderDate.setText("Order Date : "+mOrder.getDate());
        holder.tvItemOrderTotalItem.setText("Total Item : -");

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_item_order_id)
        TextView tvItemOrderId;
        @BindView(R.id.tv_item_order_date)
        TextView tvItemOrderDate;
        @BindView(R.id.tv_item_order_total_item)
        TextView tvItemOrderTotalItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
