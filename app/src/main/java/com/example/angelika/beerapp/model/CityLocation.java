package com.example.angelika.beerapp.model;

/**
 * Created by Angelika on 14.08.2018.
 */

public class CityLocation {
    // response = {"location_suggestions":
    // [{"entity_type":"city",
    // "entity_id":280,
    // "title":"New York City, New York State",
    // "latitude":40.71463,
    // "longitude":-74.005806,
    // "city_id":280,
    // "city_name":"New York City",
    // "country_id":216,
    // "country_name":"United States"}],
    // "status":"success",
    // "has_more":0,
    // "has_total":0}
    //Example :
    //   New York,New York,40.74997906,-73.98001693,13524139,New York,New York,40.74997906,-73.98001693,13524139,
    private String mCityName;
    private String mCountryName;
    private double mLat;
    private double mLng;
    private String mEntityType;
    private String mEntityId;


    public CityLocation(String aCityName, String aCountryName, String aEntityId,
                        String aEntityType, double aLat, double aLng) {
        mCityName = aCityName;
        mCountryName = aCountryName;
        mEntityType = aEntityType;
        mEntityId = aEntityId;
        mLat = aLat;
        mLng = aLng;
    }

    public CityLocation(String aEntityId,
                        String aEntityType) {
        mEntityType = aEntityType;
        mEntityId = aEntityId;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public double getLat() {
        return mLat;
    }

    public double getLng() {
        return mLng;
    }

    public String getEntityType() {
        return mEntityType;
    }

    public String getEntityId() {
        return mEntityId;
    }
}
