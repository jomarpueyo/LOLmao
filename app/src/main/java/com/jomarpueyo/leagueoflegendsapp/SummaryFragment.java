package com.jomarpueyo.leagueoflegendsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.ChampionStats;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class SummaryFragment extends Fragment {
    private static final String TAG = "SummaryFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.summary_fragment, container, false);

        ChampionDetailsTabs activity = (ChampionDetailsTabs) getActivity();
        Champion champ = activity.champData();

        Log.d("OUTPUT", champ.getLocale().toString());

        //Initialize UI
        ImageView splashImage = view.findViewById(R.id.splashImage);
        TextView nameText = view.findViewById(R.id.championName);
        TextView titleText = view.findViewById(R.id.championTitle);
        TextView loreText = view.findViewById(R.id.loreTextView);
        TextView text1 = view.findViewById(R.id.topText1);
        TextView text2 = view.findViewById(R.id.topText2);
        TextView text3 = view.findViewById(R.id.topText3);
        TextView aText = view.findViewById(R.id.allyText);
        TextView eText = view.findViewById(R.id.enemyText);

        //Set values
        loadIntoView(splashImage, champ.getSkins().get(0).getSplashImageURL());
        nameText.setText(String.valueOf(champ.getName()));
        titleText.setText(String.valueOf(champ.getTitle()));

        //Set Top Texts
        String category = "Category\n" + String.join("/", champ.getTags());
        text1.setText(category);

        String resource = "Resource Type\n" + champ.getResource();
        text2.setText(resource);

        String difficulty = "Difficulty\n" + champ.getDifficultyRating();
        text3.setText(difficulty);

        //Set Lore
        loreText.setText(adjustText(champ.getLore()));

        //Tips
        String adjust = "•" + adjustText(champ.getAllyTips().get(0));
        aText.setText(adjust);
        adjust = "•" + adjustText(champ.getEnemyTips().get(0));
        eText.setText(adjust);


        return view;
    }

    private void loadIntoView(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }

    private String adjustText(String text) {
        return "     " + text;
    }
}
