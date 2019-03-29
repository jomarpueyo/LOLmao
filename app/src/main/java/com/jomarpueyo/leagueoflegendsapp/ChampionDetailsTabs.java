package com.jomarpueyo.leagueoflegendsapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.Champion;

public class ChampionDetailsTabs extends AppCompatActivity {

    private static final String TAG = "ChampionDetailsViewPager";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    private Champion champion;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_details_tabs);

        //Locked to Portrait mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_champion_details_tabs);

        Intent intent = getIntent();
        int champ_id = Integer.parseInt(intent.getStringExtra("CHAMP_ID"));
        final Champion champ = Champion
                .withId(champ_id)
                .withRegion(Region.NORTH_AMERICA)
                .get();

        champion = champ;

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        adapter.addFragment(new SummaryFragment(), "Summary");
        adapter.addFragment(new ShopFragment(), "Shop");
        adapter.addFragment(new InfoFragment(), "Info");
        adapter.addFragment(new ItemsFragment(), "Items");
        viewPager.setAdapter(adapter);
    }

    public Champion champData(){
        return this.champion;
    }

}
