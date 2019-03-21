package com.jomarpueyo.leagueoflegendsapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class abilitiesAdapter extends RecyclerView.Adapter<abilitiesAdapter.abilitiesViewHolder> {
    private ArrayList<CardItem> mAbilitiesList;

    public static class abilitiesViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public abilitiesViewHolder(View itemView){
            super(itemView);
            mImageView = itemView.findViewById(R.id.abilityImage);
            mTextView1 = itemView.findViewById(R.id.abilityName);
            mTextView2 = itemView.findViewById(R.id.abilityDesc);
            mTextView3 = itemView.findViewById(R.id.abilityLetter);

        }
    }

    public abilitiesAdapter(ArrayList<CardItem> abilitiesList){
        mAbilitiesList = abilitiesList;
    }

    @NonNull
    @Override
    public abilitiesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spells_card, viewGroup, false);
        abilitiesViewHolder avh = new abilitiesViewHolder(v);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull abilitiesViewHolder abilitiesViewHolder, int i) {
        CardItem currentItem = mAbilitiesList.get(i);

        abilitiesViewHolder.mTextView1.setText(currentItem.getText1());
        abilitiesViewHolder.mTextView2.setText(currentItem.getText2());
        abilitiesViewHolder.mTextView3.setText(returnLetter(i));

        loadIntoView(abilitiesViewHolder.mImageView,currentItem.getUrl());
        abilitiesViewHolder.mImageView.setScaleX(2f);
        abilitiesViewHolder.mImageView.setScaleY(2f);

    }

    @Override
    public int getItemCount() {
        return mAbilitiesList.size();
    }

    private String returnLetter(int i){
        switch (i){
            case 0: return "Passive";
            case 1: return "Q";
            case 2: return "W";
            case 3: return "E";
            case 4: return "R";
        }
        return "REE";
    }

    private void loadIntoView(ImageView imageView, String url){
        Picasso.get().load(url).into(imageView);
    }
}
