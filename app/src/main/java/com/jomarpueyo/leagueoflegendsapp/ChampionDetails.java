package com.jomarpueyo.leagueoflegendsapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.ChampionSpell;
import com.merakianalytics.orianna.types.core.staticdata.SpellVariables;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChampionDetails extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout mDotLayout;
    private TextView[] mDots;
    private int currentDots;
    private Champion thisChamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_details);

        //TODO: if(Offline) status

        //Obtain champion data
        Intent intent = getIntent();
        int champID = Integer.parseInt(intent.getStringExtra("CHAMP_ID"));
        final Champion champ = Champion.withId(champID).withRegion(Region.NORTH_AMERICA).get();
        thisChamp = champ;

        //TODO: Drag for more Champion Skins
        //Initialize UI
        ImageView splashImage = findViewById(R.id.splashImage);
        TextView nameText = findViewById(R.id.championName);
        TextView titleText = findViewById(R.id.championTitle);

        //Set values
        loadIntoView(splashImage,champ.getSkins().get(0).getSplashImageURL());
        nameText.setText(String.valueOf(champ.getName()));
        titleText.setText(String.valueOf(champ.getTitle()));

        //Change text on toggle switch
        Switch toggleAbilities;
        toggleAbilities = findViewById(R.id.toggleAbilities);
        //TODO: Temporarily disabled
        toggleAbilities.setVisibility(View.GONE);

        //Populate RecyclerView
        ArrayList<CardItem> abilitiesList = new ArrayList<>();
        loadCards(abilitiesList,champ,false);

        //Load default
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ChampionDetails.super.getBaseContext());
        mAdapter = new abilitiesAdapter(abilitiesList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //On Toggle Change
        toggleAbilities.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b){
                    abilitiesList.clear();
                    loadCards(abilitiesList,champ,true);
                    toggleAbilities.setText(R.string.ToggleOnButton);
                }
                if(!b){
                    abilitiesList.clear();
                    loadCards(abilitiesList,champ,false);
                    toggleAbilities.setText(R.string.ToggleOffButton);
                }

                //Update Recycler
                mAdapter = new abilitiesAdapter(abilitiesList);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        TextView championSkinsText = findViewById(R.id.championSkinsText);
        String preText = champ.getName()+" Skins";
        championSkinsText.setText(preText);

        //Load List of Champion Skins
        ArrayList<String> imageUrls = new ArrayList<>();

        int i = 1;
        while (i<champ.getSkins().size()){
            imageUrls.add(champ.getSkins().get(i).getSplashImageURL());
            Log.d("OUTPUT", champ.getSkins().get(i).getSplashImageURL());
            i++;
        }
        currentDots = i;

        //Default Skin Image Rename
        TextView tv = findViewById(R.id.skinTitleText);
        tv.setText(thisChamp.getSkins().get(1).getName());

        //Draggable Images
        ViewPager viewPager = findViewById(R.id.viewPager);
        ImageAdapter imageAdapter = new ImageAdapter(this,imageUrls);
        viewPager.setAdapter(imageAdapter);

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);
    }

    private void addDotsIndicator(int position){
        mDotLayout = findViewById(R.id.dotsLayout);
        mDots = new TextView[currentDots-1];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparentWhite));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length>0){
            mDots[position].setTextColor(Color.WHITE);
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            //Update skin name with the skin displayed
            TextView tv = findViewById(R.id.skinTitleText);
            tv.setText(thisChamp.getSkins().get(i+1).getName());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void loadIntoView(ImageView imageView, String url){
        Picasso.get().load(url).into(imageView);
    }

    private String filterText(String s){
        //Do not fix "Redundant"
        return s.replaceAll("<\\/{0,1}font.*?>","")
                .replaceAll("<br>","\n")
                .replaceAll("<[^>]*>", "");
    }

    private void loadCards(ArrayList<CardItem> abilitiesList, Champion champ, boolean detailsOn){
    //TODO: Clean up this whole section
        //Spell Details and Ratios
        if(detailsOn){
            //Passive
            abilitiesList.add(new CardItem(
                    champ.getPassive().getImage().getURL(),
                    champ.getPassive().getName(),
                    filterText(champ.getPassive().description())));

            //QWER Spells
            for(final ChampionSpell spell: champ.getSpells()){

                String manaResource = spell.getResource();
                String resourceCost = "";
                String cooldowns = "";
                String seconds = "seconds";

                String damageToolTip = spell.getTooltip();

                //Replacing Spell Coefficients: {{ aX }}
                for(final SpellVariables var : spell.getVariables()){
                    String spellCoef = "";
                    for(double coef : var.getCoefficients()){
                        spellCoef = String.valueOf(coef);
                    }
                    damageToolTip = damageToolTip.replace("{{ "+var.getKey()+" }}", spellCoef);
                }

                //Replacing Effect Coefficients: {{ eX }}
                int j = 1;
                while(j < spell.getEffects().size()){
                    String effectHolder = "{{ e"+j+" }}";
                    String effectString = spell.getEffects().get(j).toString();

                    List<Double> list = spell.getEffects().get(j);
                    if(list.get(0).equals(list.get(list.size()-1))){
                        effectString = list.get(0).toString();
                    }

                    damageToolTip = damageToolTip.replace(effectHolder,effectString.replaceAll("\\.0",""));
                    j++;
                }

                //Replacing Ability Resources: {{ abilityResourceName }}
                if(manaResource.contains("{")){
                    manaResource=champ.getResource();
                }

                //Organizing spell cost output
                int i = 0;
                for(int cost : spell.getCosts()){

                    if(cost == 0 && champ.getResource().equals("No Cost")) break;

                    if(spell.getCosts().get(0).equals(spell.getCosts().get(2))){
                        if(manaResource.equals("No Cost")){
                            resourceCost = manaResource;
                            manaResource = "";
                            break;
                        } else {
                            resourceCost = cost +" "+ manaResource + " at all levels";
                            manaResource = "";
                            break;
                        }
                    }

                    if(i==(spell.getCosts().size()-1)){
                        resourceCost += cost+" ";
                        break;
                    }
                    resourceCost += cost+", ";
                    i++;
                }

                //Organizing Cooldown Output
                i = 0;
                for(Double cooldownNum: spell.getCooldowns()){
                    String cooldown = cooldownNum.toString();
                    cooldown = cooldown.replaceAll("\\.0","");

                    if(spell.getCooldowns().get(0).equals(spell.getCooldowns().get(2)) ){
                        cooldowns = cooldown+ " " + seconds + " at all levels";
                        seconds ="";
                        break;
                    }

                    if(i==(spell.getCosts().size()-1)){ //Last iteration remove comma
                        cooldowns += cooldown+" ";
                        break;
                    }
                    cooldowns += cooldown+", ";
                    i++;
                }

                //Add all texts to CardItem
                abilitiesList.add(new CardItem(
                        spell.getImage().getURL(),
                        spell.getName(),
                        filterText(damageToolTip)
                        +"\n"+resourceCost + manaResource
                        +"\n"+cooldowns + seconds
                        ));
            }

        }
        //Spell Descriptions
        else{
            //passive
            abilitiesList.add(new CardItem(
                    champ.getPassive().getImage().getURL(),
                    champ.getPassive().getName(),
                    filterText(champ.getPassive().description())));

            //QWER Spells
            for(final ChampionSpell spell: champ.getSpells()){
                abilitiesList.add(new CardItem(
                        spell.getImage().getURL(),
                        spell.getName(),
                        filterText(spell.getDescription())));

            }
        }

    }

}
