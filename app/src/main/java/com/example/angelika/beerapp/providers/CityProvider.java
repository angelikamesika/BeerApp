package com.example.angelika.beerapp.providers;

import android.content.res.AssetManager;

import com.example.angelika.beerapp.app.RestaurantsApplication;
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
        List<City> list = DB.INSTANCE.getCities();
        if (list.size() == 0) {
            list = importCities();
        }
        return list;
    }


    private List<City> importCities() {
        //read file from assets
        AssetManager assetManager = RestaurantsApplication.getApp().getAssets();
        InputStream is;
        List<City> list = null;
        try {
            //is = assetManager.open("cities_test");
            is = assetManager.open("cities");
            String line;
            list = new ArrayList<>();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                String[] lineSplit = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                String cityName = lineSplit[0];
                String countryName = lineSplit[5];
                double cityLat = Double.valueOf(lineSplit[2]);
                double cityLng = Double.valueOf(lineSplit[3]);
                City city = new City(cityName, countryName, cityLat, cityLng);
                //add to list
                list.add(city);
                //write to DB
                DB.INSTANCE.addCity(city);
            }
            is.close();
        } catch (IOException aE) {
            aE.printStackTrace();
        }
        return list;
    }
}
