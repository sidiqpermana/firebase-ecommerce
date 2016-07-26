package com.sidiq.intel.myshoppingmall;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sidiq.intel.myshoppingmall.adapter.CartAdapter;
import com.sidiq.intel.myshoppingmall.db.CartHelper;
import com.sidiq.intel.myshoppingmall.db.CartItem;
import com.sidiq.intel.myshoppingmall.event.RefreshCartEvent;
import com.sidiq.intel.myshoppingmall.model.Order;
import com.sidiq.intel.myshoppingmall.model.OrderItem;
import com.sidiq.intel.myshoppingmall.preference.AppPreference;
import com.sidiq.intel.myshoppingmall.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends AppCompatActivity
    implements View.OnClickListener{

    @BindView(R.id.lv_item)
    ListView lvItem;
    @BindView(R.id.tv_total_qty)
    TextView tvTotalQty;
    @BindView(R.id.tv_total_pay)
    TextView tvTotalPay;
    @BindView(R.id.btn_submit_order)
    Button btnSubmitOrder;
    @BindView(R.id.ln_cart_summary)
    LinearLayout lnCartSummary;

    private ArrayList<CartItem> list;
    private CartHelper mCartHelper;
    private CartAdapter adapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Create Order");
        mProgressDialog.setMessage("Please wait...");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("order");

        btnSubmitOrder.setOnClickListener(this);

        mCartHelper = new CartHelper(CartActivity.this);
        list = mCartHelper.getAll();

        lnCartSummary.setVisibility(View.GONE);

        adapter = new CartAdapter(CartActivity.this);
        if (list != null) {
            if (list.size() > 0) {
                lnCartSummary.setVisibility(View.VISIBLE);
                adapter.setList(list);
                showTotalTransactionInfo();
            } else {
                adapter.setList(new ArrayList<CartItem>());
                Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            adapter.setList(new ArrayList<CartItem>());
            Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
        }

        lvItem.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTotalTransactionInfo(){
        lnCartSummary.setVisibility(View.GONE);
        ArrayList<CartItem> list = mCartHelper.getAll();
        if (list != null){
            if (list.size() > 0){
                lnCartSummary.setVisibility(View.VISIBLE);
                int totalQty = 0;
                int totalPay = 0;

                for (CartItem item : list){
                    totalQty += item.getQty();
                    totalPay += (item.getQty() * item.getPrice());
                }

                tvTotalQty.setText("Total Qty : "+totalQty);
                tvTotalPay.setText("Total Pay : "+totalPay);
            }

        }
    }

    @Subscribe
    public void onEvent(RefreshCartEvent event){
        showTotalTransactionInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit_order){
            firebasePostOrder();
        }
    }

    private void firebasePostOrder() {
        mProgressDialog.show();

        AppPreference appPreference = new AppPreference(this);

        Order mOrder = new Order();
        mOrder.setDate(Util.getCurrentDate());
        mOrder.setOrder_id(System.currentTimeMillis());
        mOrder.setUsername(TextUtils.isEmpty(appPreference.getUsername()) ? appPreference.getEmail() : appPreference.getUsername());
        mOrder.setUser_id(appPreference.getUserId());

        OrderItem orderItem;
        List<OrderItem> listOrderItems = new ArrayList<>();
        for (CartItem item : list){
            orderItem = new OrderItem();
            orderItem.setQty(item.getQty());
            orderItem.setName(item.getName());
            orderItem.setPrice((int)item.getPrice());
            orderItem.setProduct_id(item.getId());

            listOrderItems.add(orderItem);
        }
        mOrder.setList(listOrderItems);

        mDatabaseReference.push().setValue(mOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mProgressDialog.cancel();
                if (task.isSuccessful()){
                    Toast.makeText(CartActivity.this, "Create order success", Toast.LENGTH_LONG).show();
                    mCartHelper.clear();

                    finish();

                }else{
                    Toast.makeText(CartActivity.this, "Create order failed", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
