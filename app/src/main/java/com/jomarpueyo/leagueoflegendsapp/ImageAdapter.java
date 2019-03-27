package com.jomarpueyo.leagueoflegendsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

//TODO: https://www.youtube.com/watch?v=Q2FPDI99-as
public class ImageAdapter extends PagerAdapter {

    private Context mContext;
    private int[] mImageIds = new int[]{};

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return false;
    }
}
