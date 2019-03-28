package com.jomarpueyo.leagueoflegendsapp;

import android.content.Context;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imageUrls;

    ImageAdapter(Context context, ArrayList<String> imageUrls){
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageButton imageButton = new ImageButton(context);
        imageButton.setBackgroundColor(Color.parseColor("#FAFAFA"));

        Picasso.get()
                .load(imageUrls.get(position))
                .fit()
                .into(imageButton);
        container.addView(imageButton);

        return imageButton;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}
