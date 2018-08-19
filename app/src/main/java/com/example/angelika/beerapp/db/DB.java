package com.example.angelika.beerapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.angelika.beerapp.app.RestaurantsApplication;
import com.example.angelika.beerapp.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelika on 02.08.2018.
 */

public enum DB {
    INSTANCE;

    private SQLiteDatabase mRO;
    private SQLiteDatabase mRW;

    private final Object mLock;

    DB() {
        mLock = new Object();
        DBHelper helper = new DBHelper(RestaurantsApplication.getApp());
        mRO = helper.getReadableDatabase();
        mRW = helper.getWritableDatabase();
    }

    // ======================== CITIES

    public void addCity(City aCity) {
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CITY_NAME, aCity.getCityName());
        cv.put(DBHelper.CITY_COUNTRY, aCity.getCountryName());
        cv.put(DBHelper.CITY_LAT, aCity.getLat());
        cv.put(DBHelper.CITY_LNG, aCity.getLng());


        synchronized (mLock) {
            mRW.insert(DBHelper.TABLE_CITIES, null, cv);
        }
    }

    public List<City> getCities() {
        List<City> result = new ArrayList<>();
        String sQuery = "select * from " + DBHelper.TABLE_CITIES;

        Cursor c = mRO.rawQuery(sQuery, null);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    int idIndex = c.getColumnIndex(BaseColumns._ID);
                    int cityIdIndex = c.getColumnIndex(DBHelper.CITY_NAME);
                    int cityAsciiIdIndex = c.getColumnIndex(DBHelper.CITY_NAME);
                    int countryIdIndex = c.getColumnIndex(DBHelper.CITY_COUNTRY);
                    int latIdIndex = c.getColumnIndex(DBHelper.CITY_LAT);
                    int lngIdIndex = c.getColumnIndex(DBHelper.CITY_LNG);

                    do {

                        long id = c.getLong(idIndex);
                        String cityName = c.getString(cityIdIndex);
                        String cityNameAscii = c.getString(cityAsciiIdIndex);
                        String country = c.getString(countryIdIndex);
                        Double lat = Double.valueOf(c.getString(latIdIndex));
                        Double lng = Double.valueOf(c.getString(lngIdIndex));
                        result.add(new City(cityName, cityNameAscii, country, lat, lng));
                    } while (c.moveToNext());
                }
            } finally {
                c.close();
            }
        }
        Log.d("AAA_db", "getCities size = " + result.size());
        return result;
    }


//    public void updateChannelRss(String aSectionId, String aChannelId, String aRss) {
//        ContentValues cv = new ContentValues();
//        cv.put(DBHelper.FEED_SECTION_ID, aSectionId);
//        cv.put(DBHelper.FEED_CHANNEL_ID, aChannelId);
//        cv.put(DBHelper.FEED_RSS, aRss);
//        cv.put(DBHelper.FEED_UPDATE_TIME, System.currentTimeMillis());
//
//        synchronized (mLock) {
//            int c = mRW.update(DBHelper.TABLE_FEED, cv,
//                    DBHelper.FEED_SECTION_ID + "='" + aSectionId +
//                            "' and " + DBHelper.FEED_CHANNEL_ID + "='" + aChannelId + "'",
//                    null);
//            if (c == 0) {
//                mRW.insert(DBHelper.TABLE_FEED, null, cv);
//            }
//        }
   }