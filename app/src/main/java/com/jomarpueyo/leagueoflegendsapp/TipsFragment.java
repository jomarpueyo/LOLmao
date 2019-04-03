package com.jomarpueyo.leagueoflegendsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.merakianalytics.orianna.types.core.staticdata.Champion;

public class TipsFragment extends Fragment {
    private static final String TAG = "ItemsFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tips_fragment, container, false);

        ChampionDetailsTabs activity = (ChampionDetailsTabs) getActivity();
        Champion champ = activity.champData();

        //Initialize UI
        TextView aText = view.findViewById(R.id.allyText);
        TextView eText = view.findViewById(R.id.enemyText);
        TextView aTitle = view.findViewById(R.id.aTips);
        TextView eTitle = view.findViewById(R.id.eTips);

        //Text Change
        String temp = "Playing as " + champ.getName();
        aTitle.setText(temp);
        temp = "Playing Against " + champ.getName();
        eTitle.setText(temp);

        String adjust = "• " + String.join("\n\n• ", champ.getAllyTips());
        aText.setText(adjust);
        adjust = "• " + String.join("\n\n• ", champ.getEnemyTips());
        eText.setText(adjust);

        return view;
    }

}
