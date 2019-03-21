package com.jomarpueyo.leagueoflegendsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.ChampionSpell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


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

        //Populate RecyclerView
        ArrayList<CardItem> abilitiesList = new ArrayList<>();

        //Passive
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

        //Update Recycler
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new abilitiesAdapter(abilitiesList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Pica picasso, bitch.
    private void loadIntoView(ImageView imageView, String url){
        Picasso.get().load(url).into(imageView);
    }

    private String filterText(String s){
        //Do not fix "Redundant"
        return s.replaceAll("<\\/{0,1}font.*?>","")
                .replaceAll("<br>","\n");
    }

}
