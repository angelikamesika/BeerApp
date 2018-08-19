package com.example.angelika.beerapp.model;

import android.support.annotation.NonNull;

/**
 * Created by Angelika on 16.08.2018.
 */

public class RestaurantInfo implements Comparable<RestaurantInfo>{
    private String mInfo;
    private int mPlace;

    public RestaurantInfo(String aInfo, int aPlace) {
        mInfo = aInfo;
        mPlace = aPlace;
    }

    public String getInfo() {
        return mInfo;
    }

    public int getPlace() {
        return mPlace;
    }



    @Override
    public int compareTo(@NonNull RestaurantInfo aRestaurantInfo) {
        int place = aRestaurantInfo.getPlace();
        return this.mPlace - place;
    }
}
