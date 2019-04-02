package com.jomarpueyo.leagueoflegendsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopFragment extends Fragment {
    private static final String TAG = "ShopFragment";

    private int currentDots;
    private Champion thisChamp;
    private LinearLayout mDotLayout;
    private TextView tv;
    private ImageView loadingView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_fragment, container, false);

        ChampionDetailsTabs activity = (ChampionDetailsTabs) getActivity();
        Champion champ = activity.champData();
        thisChamp = champ;

        //TODO: New viewpager for loading screens (no dots)
        //Load List of Champion Skins
        ArrayList<String> imageUrls = new ArrayList<>();

        int i = 1;
        while (i < champ.getSkins().size()) {
            imageUrls.add(champ.getSkins().get(i).getSplashImageURL());
            i++;
        }
        currentDots = i;

        tv = view.findViewById(R.id.skinTitleText);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        mDotLayout = view.findViewById(R.id.dotsLayout);
        loadingView = view.findViewById(R.id.loadingImg);


        //Default Skin Image Rename
        tv.setText(thisChamp.getSkins().get(1).getName());
        loadIntoView(loadingView, thisChamp.getSkins().get(1).getLoadingImageURL());
        loadingView.setScaleX(1.5f);
        loadingView.setScaleY(1.5f);

        //Draggable Images
        viewPager.setAdapter(new ImageAdapter(getActivity(), imageUrls));

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

        return view;
    }

    private void addDotsIndicator(int position) {
        TextView[] mDots = new TextView[currentDots - 1];

        mDotLayout.removeAllViews();

        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this.getActivity());
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(Color.WHITE);
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            //Update Image
            addDotsIndicator(i);

            //Update skin name with the skin displayed
            tv.setText(thisChamp.getSkins().get(i + 1).getName());
            loadIntoView(loadingView, thisChamp.getSkins().get(i + 1).getLoadingImageURL());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void loadIntoView(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }
}
