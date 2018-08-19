package com.example.angelika.beerapp.model;

/**
 * Created by Angelika on 14.08.2018.
 */

public class Restaurant {
    @Input(name="Name", place=0)
    private String mName;

    @Input(name="Address", place=1)
    private String mAddress;

    private double mLat;
    private double mLng;

    @Input(name="Cuisines", place=3)
    private String mCuisines;

    @Input(name="Average cost for two", place=4)
    private String mAverageCostForTwo;

    @Input(name="Currency", place=5)
    private String mCurrency;

    @Input(name="Price range", place=6)
    private String mPriceRange;

    @Input(name="User rating", place=7)
    private String mUserRating;

    private String mPhotoURL;

    public String getName() {
        return mName;
    }

    public void setName(String aName) {
        mName = aName;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String aAddress) {
        mAddress = aAddress;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double aLat) {
        mLat = aLat;
    }

    public double getLng() {
        return mLng;
    }

    public void setLng(double aLng) {
        mLng = aLng;
    }

    public String getCuisines() {
        return mCuisines;
    }

    public void setCuisines(String aCuisines) {
        mCuisines = aCuisines;
    }

    public String getAverageCostForTwo() {
        return mAverageCostForTwo;
    }

    public void setAverageCostForTwo(String aAverageCostForTwo) {
        mAverageCostForTwo = aAverageCostForTwo;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String aCurrency) {
        mCurrency = aCurrency;
    }

    public String getPriceRange() {
        return mPriceRange;
    }

    public void setPriceRange(String aPriceRange) {
        mPriceRange = aPriceRange;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public void setUserRating(String aUserRating) {
        mUserRating = aUserRating;
    }

    public String getPhotoURL() {
        return mPhotoURL;
    }

    public void setPhotoURL(String aPhotoURL) {
        mPhotoURL = aPhotoURL;
    }
}
