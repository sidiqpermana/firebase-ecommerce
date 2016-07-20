package com.sidiq.intel.myshoppingmall;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class DetailImageActivity extends AppCompatActivity {
    private ArrayList<String> listImageUrl;
    private int selectedPosition;
    private ViewPager mViewPager;
    private ImagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);

        getSupportActionBar().hide();
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        listImageUrl = getIntent().getStringArrayListExtra("url_images");
        selectedPosition = getIntent().getIntExtra("position", 0);

        adapter = new ImagePagerAdapter(getSupportFragmentManager());
        adapter.setListImages(listImageUrl);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(selectedPosition);
    }
}
