package com.example.angelika.beerapp.providers;

import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

/**
 * Created by Angelika on 16.08.2018.
 */

public interface LocationDetailsListener extends JSONObjectRequestListener {
    void doOnLocationDetailsResponse(JSONObject response);
}
