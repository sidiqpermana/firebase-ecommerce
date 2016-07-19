package com.sidiq.intel.myshoppingmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity
    implements AdapterView.OnItemClickListener{
    private ListView lvItem;
    private ProductAdapter adapter;
    private ArrayList<Product> listProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        String selectedCategory = getIntent().getStringExtra("category");
        getSupportActionBar().setTitle(selectedCategory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvItem = (ListView)findViewById(R.id.lv_item);
        lvItem.setOnItemClickListener(this);
        listProduct = new ArrayList<>();
        adapter = new ProductAdapter(ProductActivity.this);
        adapter.setListItem(listProduct);

        Product mProduct = null;
        for (int i = 0; i < SampleData.boots.length; i++){
            mProduct = new Product();
            mProduct.setId(System.currentTimeMillis());
            mProduct.setName(SampleData.boots[i][0]);
            mProduct.setPrice(SampleData.boots[i][2]);
            mProduct.setImageUrl(SampleData.boots[i][1]);

            listProduct.add(mProduct);
        }

        adapter.setListItem(listProduct);
        adapter.notifyDataSetChanged();
        lvItem.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(ProductActivity.this, DetailProductActivity.class);
        intent.putExtra("product", listProduct.get(i));
        startActivity(intent);
    }
}
