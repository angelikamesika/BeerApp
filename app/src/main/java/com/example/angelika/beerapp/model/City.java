package com.example.angelika.beerapp.model;

/**
 * Created by Angelika on 02.08.2018.
 */

public class City {
    private String mCityName;
    private String mCityNameAscii;
    private String mCountryName;
    private String mIso2;
    private String mIso3;
    private Double mLat;
    private Double mLng;

    public City(String aCityName, String aCityNameAscii, String aCountryName, Double aLat, Double aLng) {
        mCityName = aCityName;
        mCityNameAscii = aCityNameAscii;
        mCountryName = aCountryName;
        mLat = aLat;
        mLng = aLng;
    }
    public City(String aCityName, String aCountryName, Double aLat, Double aLng) {
        mCityName = aCityName;
        mCountryName = aCountryName;
        mLat = aLat;
        mLng = aLng;
    }

    public  City(){

    }


    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String aCityName) {
        mCityName = aCityName;
    }

    public String getCityNameAscii() {
        return mCityNameAscii;
    }

    public void setCityNameAscii(String aCityNameAscii) {
        mCityNameAscii = aCityNameAscii;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public void setCountryName(String aCountryName) {
        mCountryName = aCountryName;
    }

    public String getIso2() {
        return mIso2;
    }

    public void setIso2(String aIso2) {
        mIso2 = aIso2;
    }

    public String getIso3() {
        return mIso3;
    }

    public void setIso3(String aIso3) {
        mIso3 = aIso3;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double aLat) {
        mLat = aLat;
    }

    public Double getLng() {
        return mLng;
    }

    public void setLng(Double aLng) {
        mLng = aLng;
    }




    //    public static final String CITY_NAME = "city";
//    public static final String CITY_NAME_ASCII = "city_ascii";
//    public static final String CITY_LAT = "lat";
//    public static final String CITY_LNG = "lng";
//    public static final String CITY_COUNTRY = "country";
//    public static final String CITY_ISO2 = "iso2";
//    public static final String CITY_ISO3 = "iso3";


}
