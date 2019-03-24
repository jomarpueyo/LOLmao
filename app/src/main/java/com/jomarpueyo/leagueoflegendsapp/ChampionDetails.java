package com.jomarpueyo.leagueoflegendsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.ChampionSpell;
import com.merakianalytics.orianna.types.core.staticdata.SpellVariables;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class ChampionDetails extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_details);

        //TODO: if(Offline) status

        //Obtain champion data
        Intent intent = getIntent();
        int champID = Integer.parseInt(intent.getStringExtra("CHAMP_ID"));
        final Champion champ = Champion.withId(champID).withRegion(Region.NORTH_AMERICA).get();

        //TODO: Drag for more Champion Skins
        //Initialize UI
        ImageView splashImage = findViewById(R.id.splashImage);
        TextView nameText = findViewById(R.id.championName);
        TextView titleText = findViewById(R.id.championTitle);
        //TODO: Add More Flare to "Abilities" Title

        //Set values
        loadIntoView(splashImage,champ.getSkins().get(0).getSplashImageURL());
        nameText.setText(String.valueOf(champ.getName()));
        titleText.setText(String.valueOf(champ.getTitle()));

        //Change text on toggle switch
        Switch toggleAbilities;
        toggleAbilities = findViewById(R.id.toggleAbilities);

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
                mRecyclerView = findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ChampionDetails.super.getBaseContext());
                mAdapter = new abilitiesAdapter(abilitiesList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

            }
        });

    }

    //Pica picasso, bitch.
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

//        String myString = thisChamp.getSpells().get(1).getCooldownBurn(); //TODO: LMAO TIME TO REFORMAT EVERYTHINNGGGGG

        //TODO: Add damage and ratios
        if(detailsOn){ //Spell Ratios
            //Passive
            abilitiesList.add(new CardItem(
                    champ.getPassive().getImage().getURL(),
                    champ.getPassive().getName(),
                    filterText(champ.getPassive().description())));

            //TODO: Clean up
            //QWER Spells
            for(final ChampionSpell spell: champ.getSpells()){
            //TODO: Stringbuilder (Clean-up)
                String manaResource = spell.getResource();
                String resourceCost = "";
                String cooldowns = "";
                String seconds = "seconds";

                //TODO: Make a hashlibrary for each one???? Ask League Discord For Help
                String damageToolTip = spell.getTooltip();
                damageToolTip = damageToolTip.replace("{{ qdamage }}", "QReplaced");

                for(final SpellVariables var : spell.getVariables()){
                    StringBuilder sb = new StringBuilder();
                    for(double coef : var.getCoefficients()){
                        Log.d("OUTPUT","Coef: "+ String.valueOf(coef));
                        sb.append(String.valueOf(coef));
                    }
                    damageToolTip = damageToolTip.replace("{{ "+var.getKey()+" }}", sb);

                    for(List<Double> list : spell.getEffects()){
                        Log.d("OUTPUT",list.get(0).toString());
                    }


                }



                if(manaResource.contains("{")){ //{{ abilityresourcename }}
                    manaResource=champ.getResource();
                }

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

                abilitiesList.add(new CardItem(
                        spell.getImage().getURL(),
                        spell.getName(),
                        filterText(damageToolTip)
                        +"\n"+resourceCost + manaResource
                        +"\n"+cooldowns + seconds
                        ));
            }

        }
        else{   //Spell Description
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
