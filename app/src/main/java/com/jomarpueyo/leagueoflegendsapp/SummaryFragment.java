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

        //Initialize UI
        ImageView splashImage = view.findViewById(R.id.splashImage);
        TextView nameText = view.findViewById(R.id.championName);
        TextView titleText = view.findViewById(R.id.championTitle);
        TextView champValues = view.findViewById(R.id.champValues);
        ImageView loadingImg = view.findViewById(R.id.loadingImg);
        TextView loreText = view.findViewById(R.id.loreTextView);
        TextView scaleText = view.findViewById(R.id.champScales);

        //Set values
        loadIntoView(splashImage, champ.getSkins().get(0).getSplashImageURL());
        nameText.setText(String.valueOf(champ.getName()));
        titleText.setText(String.valueOf(champ.getTitle()));

        Log.d("OUTPUT", champ.getSkins().get(0).getLoadingImageURL());

        //Set Champion Values
        String champRatings = "Role: " + String.join("/", champ.getTags());

        champRatings += "\nResource Type: " + champ.getResource()
                + "\nAttack Rating: " + champ.getPhysicalRating()
                + "\nMagic Rating: " + champ.getMagicRating()
                + "\nDefense Rating: " + champ.getDefenseRating()
                + "\nDifficulty: " + champ.getDifficultyRating();
        champValues.setText(champRatings);

        //TODO: Champion Stats
        ChampionStats cs = champ.getStats();
        String champScales =
                "Health " + cs.getHealth() + " - " + (cs.getHealth() + cs.getHealthPerLevel() * 18)
                        + "\nHealth Regen. " + cs.getHealthRegen() + " - " + (cs.getHealthRegen() + cs.getHealthRegenPerLevel() * 18)
                        + "\nMana " + cs.getMana() + " - " + (cs.getMana() + cs.getManaPerLevel() * 18)
                        + "\nMana Regen. " + cs.getManaRegen() + " - " + (cs.getManaRegen() + cs.getManaRegenPerLevel() * 18);

        Log.d("OUTPUT", champScales);


        //Set Champion Loading Image
        loadIntoView(loadingImg, champ.getSkins().get(0).getLoadingImageURL());
        loadingImg.setScaleX(1.5f);
        loadingImg.setScaleY(1.5f);

        //Set Lore
        String adjust = "     " + champ.getLore();
        loreText.setText(adjust);

        return view;
    }

    private void loadIntoView(ImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }
}
