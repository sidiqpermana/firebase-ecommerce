package com.sidiq.intel.myshoppingmall.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sidiq.intel.myshoppingmall.util.CustomOnItemClickListener;
import com.sidiq.intel.myshoppingmall.util.CustomOnItemSelectedListener;
import com.sidiq.intel.myshoppingmall.R;
import com.sidiq.intel.myshoppingmall.event.RefreshCartEvent;
import com.sidiq.intel.myshoppingmall.db.CartHelper;
import com.sidiq.intel.myshoppingmall.db.CartItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by inte on 7/22/2016.
 */
public class CartAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<CartItem> list;
    private CartHelper mCartHelper;

    private String[] qtyOptions = new String[]{
            "1", "2", "3", "4", "5"
    };

    public CartAdapter(Activity activity) {
        this.activity = activity;
        mCartHelper = new CartHelper(activity);
    }

    public ArrayList<CartItem> getList() {
        return list;
    }

    public void setList(ArrayList<CartItem> list) {
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
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_row_cart, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        CartItem item = (CartItem)getItem(i);
        holder.tvProductName.setText(item.getName());
        holder.tvProductPrice.setText(String.valueOf(item.getPrice()));
        Glide.with(activity)
                .load(item.getImage())
                .into(holder.imgProduct);
        int subTotal = getSubTotal(item.getQty(), item.getPrice());
        holder.tvProductSubTotal.setText("Sub total "+String.valueOf(subTotal));

        holder.spnQty.setAdapter(new ArrayAdapter<String>(activity,
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1,
                qtyOptions));

        int selectedQtyPosition = 0;
        for (int j = 0; j < qtyOptions.length; j++){
            if (Integer.parseInt(qtyOptions[j]) == item.getQty()){
                selectedQtyPosition = j;
                break;
            }
        }
        holder.spnQty.setSelection(selectedQtyPosition);

        holder.imgDelete.setOnClickListener(new CustomOnItemClickListener(i,
                new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                mCartHelper.delete(getList().get(position).getId());
                getList().remove(position);
                notifyDataSetChanged();
                Toast.makeText(activity, "One item is successfully deleted", Toast.LENGTH_SHORT).show();

                RefreshCartEvent event = new RefreshCartEvent();
                event.setEventMessage("Delete item");

                EventBus.getDefault().post(event);
            }
        }));

        holder.spnQty.setOnItemSelectedListener(new CustomOnItemSelectedListener(i, holder.tvProductSubTotal,
                new CustomOnItemSelectedListener.OnItemSelectedCallback() {
            @Override
            public void onItemSelected(View view, TextView textView, int itemRowPosition, int itemArrayPosition) {
                int selectedQty = Integer.parseInt(qtyOptions[itemArrayPosition]);
                int productQty = getList().get(itemRowPosition).getQty();

                if (selectedQty != productQty){
                    mCartHelper.update(Integer.parseInt(qtyOptions[itemArrayPosition]),
                            getList().get(itemRowPosition).getId());

                    int updatedSubtotal = getSubTotal(selectedQty,
                            (int)getList().get(itemRowPosition).getPrice());
                    textView.setText("Sub total "+updatedSubtotal);

                    RefreshCartEvent event = new RefreshCartEvent();
                    event.setEventMessage("Update qty "+selectedQty+" on productId : "+getList().get(itemRowPosition).getId());

                    EventBus.getDefault().post(event);
                }

            }
        }));

        return view;
    }

    private int getSubTotal(int subTotalQty, double price){
        return subTotalQty * (int)price;
    }

    static class ViewHolder {
        @BindView(R.id.img_delete)
        ImageView imgDelete;
        @BindView(R.id.img_product)
        ImageView imgProduct;
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_product_price)
        TextView tvProductPrice;
        @BindView(R.id.spn_qty)
        Spinner spnQty;
        @BindView(R.id.tv_product_sub_total)
        TextView tvProductSubTotal;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
