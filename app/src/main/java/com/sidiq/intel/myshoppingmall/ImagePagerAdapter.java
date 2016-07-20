package com.sidiq.intel.myshoppingmall;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by inte on 7/20/2016.
 */
public class ImagePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> listImages;

    public ArrayList<String> getListImages() {
        return listImages;
    }

    public void setListImages(ArrayList<String> listImages) {
        this.listImages = listImages;
    }

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ImageFragment mImageFragment = new ImageFragment();
        mImageFragment.setImageUrl(getListImages().get(position));
        return mImageFragment;
    }

    @Override
    public int getCount() {
        return getListImages().size();
    }
}
