package com.example.angelika.beerapp.providers;

import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

/**
 * Created by Angelika on 16.08.2018.
 */

public interface OnLocationListener extends JSONObjectRequestListener {
    void onLocationResponse(JSONObject aResponse);
    void onLocationError(ANError aANError);

}
