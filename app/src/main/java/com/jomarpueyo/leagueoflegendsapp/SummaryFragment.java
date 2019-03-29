package com.jomarpueyo.leagueoflegendsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.squareup.picasso.Picasso;

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

        //Set values
        loadIntoView(splashImage,champ.getSkins().get(0).getSplashImageURL());
        nameText.setText(String.valueOf(champ.getName()));
        titleText.setText(String.valueOf(champ.getTitle()));

        return view;
    }

    private void loadIntoView(ImageView imageView, String url){
        Picasso.get().load(url).into(imageView);
    }
}
