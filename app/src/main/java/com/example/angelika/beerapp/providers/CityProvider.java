package com.example.angelika.beerapp.providers;

import android.content.res.AssetManager;
import android.util.Log;

import com.example.angelika.beerapp.app.BeerApplication;
import com.example.angelika.beerapp.db.DB;
import com.example.angelika.beerapp.model.City;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelika on 16.08.2018.
 */

public class CityProvider {

    public List<City> getCities() {
        return getCityListFromDB();
    }


    private List<City> downloadCities() {
        //read file from assets
        // city,city_ascii,lat,lng,pop,country,iso2,iso3,province
        AssetManager assetManager = BeerApplication.getApp().getAssets();
        InputStream is = null;
        List<City> list = null;
        try {
            //is = assetManager.open("cities_test");
            is = assetManager.open("cities");
            String line;
            list = new ArrayList<>();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",");
                String cityName = lineSplit[0];
                String countryName = lineSplit[5];
                double cityLat = Double.valueOf(lineSplit[2]);
                double cityLng = Double.valueOf(lineSplit[3]);
                City city = new City(cityName, countryName, cityLat, cityLng);
                list.add(city);
                //write to DB
                DB.INSTANCE.addCity(city);
            }
            is.close();
        } catch (IOException aE) {
            Log.d("AAA_DB", "downloadCities EXCEPTION " + aE.getMessage());
            aE.printStackTrace();
        }
        Log.d("AAA_DB", "downloadCities() 111 list fromDB = " + list.size());
        return list;

    }

    private List<City> getCityListFromDB() {
        List<City> list = DB.INSTANCE.getCities();
        Log.d("AAA_DB", "getCityListFromDB 000 list fromDB = " + list.size());
        if (list.size() == 0) {
            list = downloadCities();
        }
        Log.d("AAA_DB", "getCityListFromDB 222 list fromDB = " + list.size());
        return list;
    }


}
