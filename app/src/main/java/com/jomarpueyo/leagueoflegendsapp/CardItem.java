package com.jomarpueyo.leagueoflegendsapp;

public class CardItem {
    private String mUrl;
    private String mText1;
    private String mText2;

    public CardItem(String url, String text1, String text2){
        mUrl = url;
        mText1 = text1;
        mText2 = text2;
    }

    public String getUrl(){
        return mUrl;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

}
