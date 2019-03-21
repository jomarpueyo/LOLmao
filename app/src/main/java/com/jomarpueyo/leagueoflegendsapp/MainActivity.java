package com.jomarpueyo.leagueoflegendsapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.Champions;
import com.merakianalytics.orianna.types.core.staticdata.Versions;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    Boolean onlineMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: This does not appear before loading all the champions?
        Toast.makeText(this,"Loading...", Toast.LENGTH_SHORT);
        ProgressBar progressBar = findViewById(R.id.progress_loader);
        progressBar.setVisibility(View.VISIBLE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //TODO: Add support for multi-regional (For languages)
        //TODO: Clean this code section up
        TextView textView = findViewById(R.id.textView);
        String thisVersion = "";
        try{
            final Versions versions = Versions.withRegion(Region.NORTH_AMERICA).get();
            //TODO: Fix this "Loop" for one time version obtain.
            for(final String version : versions){
                thisVersion = version;
                break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            onlineMode = false;
        }

        //TODO: Clean-up img loading process

        GridLayout mainGrid = findViewById(R.id.mainGrid);
        if(onlineMode){
            textView.append(" "+thisVersion);

            //populate grid
            final Champions champions = Champions.withRegion(Region.NORTH_AMERICA).get();
            for(final Champion champion : champions){
                //Assign image button properties here
                ImageButton newIMGButton = new ImageButton(this);
                newIMGButton.setTag(champion.getId());
                loadGridImage(newIMGButton);
                loadIntoView(newIMGButton, champion.getImage().getURL());
            }
        }
        else{ //offline
            textView.append(" 3? Maybe 4?");

            ImageButton newIMGButton = new ImageButton(this);
            newIMGButton.setScaleY(2);
            newIMGButton.setScaleX(2);
            loadGridImage(newIMGButton);

            Toast.makeText(this, "You're offline!", Toast.LENGTH_LONG).show();
        }
        //Process done, hide pb
        progressBar.setVisibility(View.GONE);

        setSingleEvent(mainGrid);
    }
    //TODO: Change grid size,
    private void setSingleEvent(GridLayout mainGrid){
        int childCount = mainGrid.getChildCount();

        for(int i = 0; i <childCount; i++){
            ImageButton imageButton = (ImageButton) mainGrid.getChildAt(i);
            String passTag = mainGrid.getChildAt(i).getTag().toString();

            imageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(MainActivity.this, ChampionDetails.class);
                    intent.putExtra("CHAMP_ID",passTag);
                    startActivity(intent);
                }
            });
        }
    }

    //Pica picasso, bitch.
    private void loadIntoView(ImageView imageView, String url){
        Picasso.get().load(url).into(imageView);
    }

    //Additive grid layout - More champions, no problem.
    private void loadGridImage(ImageButton imageButton){
        GridLayout mainGrid = findViewById(R.id.mainGrid);
        imageButton.setImageResource(R.mipmap.teemo_sqimg);
        imageButton.setScaleX(1.5f);
        imageButton.setScaleY(1.5f);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
        imageButton.setPadding(40,40,40,40);
        imageButton.isClickable();
        mainGrid.addView(imageButton);
    }








}