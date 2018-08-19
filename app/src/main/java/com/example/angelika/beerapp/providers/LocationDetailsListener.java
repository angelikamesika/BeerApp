package com.example.angelika.beerapp.providers;

import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.angelika.beerapp.model.City;
import com.example.angelika.beerapp.model.Restaurant;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Angelika on 16.08.2018.
 */

public interface LocationDetailsListener extends JSONObjectRequestListener {
    void doOnLocationDetailsResponse(City aCity, List<Restaurant> aList);
    void showNotFoundLocation(City aCity);
}
