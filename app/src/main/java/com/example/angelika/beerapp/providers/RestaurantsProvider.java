package com.example.angelika.beerapp.providers;

import android.location.Location;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.angelika.beerapp.app.BeerApplication;
import com.example.angelika.beerapp.model.City;
import com.example.angelika.beerapp.model.CityLocation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Angelika on 16.08.2018.
 */

public class RestaurantsProvider {
    private final String KEY = "885cdf0f3c2235a1a5792084ddc74a9b";
    private final String LOCATION = "https://developers.zomato.com/api/v2.1/locations";
    private final String LOCATION_DETAILS = "https://developers.zomato.com/api/v2.1/location_details";
    private static final int MAX_RADIUS = 10000;

    private ResponseLocationNotFound mResponseLocationNotFound;


    public ResponseLocationNotFound getResponseLocationNotFound() {
        return mResponseLocationNotFound;
    }

    public void setResponseLocationNotFound(ResponseLocationNotFound aResponseLocationNotFound) {
        mResponseLocationNotFound = aResponseLocationNotFound;
    }

    public void getLocationCity(City aCity, final ResponseLocationNotFound aResponseLocationNotFound,
                                final LocationDetailsListener aLocationDetailsListener) {
        Log.d("AAA_network", "getLocationCity !!! city = " + aCity.getCityName());
        AndroidNetworking.get(LOCATION)
                //  .addPathParameter("pageNumber", "0")
                .addQueryParameter("user-key", KEY)
                .addQueryParameter("query", /*"New York"*/aCity.getCityName())


                .addHeaders("user-key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new LocationListenerImp(aCity, aLocationDetailsListener, aResponseLocationNotFound));
//                        new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        Log.d("AAA_network", "onResponse response = " + response);
//                        doOnLocationResponse(response, aLocationDetailsListener, aResponseLocationNotFound);
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        String  body= error.getErrorBody();
//                        Log.d("AAA_network", "onError error = " + body);
//                    }
//                });
    }

    private void _doOnLocationResponse(JSONObject response, LocationDetailsListener aLocationDetailsListener, ResponseLocationNotFound
            aResponseLocationNotFound) {
        Log.d("AAA_network", "doOnLocationResponse !!!");
        try {
            JSONArray arraySuggestions = response.getJSONArray("location_suggestions");
//            String countryName = response.getString("country_name");
//            //TODO if country Name not like in request stop
//            String entity_type = response.getString("entity_type");
//            String entity_id = response.getString("entity_id");
//            CityLocation location = new CityLocation(entity_type, entity_id);
            sendRequestLocationDetails(null, aLocationDetailsListener);

        } catch (JSONException aE) {
            Log.d("AAA_network", "doOnLocationResponse JSONException aE = " + aE.getMessage());
            aE.printStackTrace();
        }


    }

    private void sendRequestLocationDetails(CityLocation aLocation, LocationDetailsListener aListener) {
        AndroidNetworking.get(LOCATION_DETAILS)
                //  .addPathParameter("pageNumber", "0")
                //  .addQueryParameter("user-key", KEY)
                .addQueryParameter("entity_type", /*"group"*/aLocation.getEntityType())
                .addQueryParameter("entity_id", /*"36932"*/aLocation.getEntityId())
                .addHeaders("user-key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(aListener);
//                        new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // do anything with response
//                        Log.d("AAA_network", "onResponse DETAILS response = " + response);
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        String  body= error.getErrorBody();
//                        Log.d("AAA_network", "onError DETAILS error = " + body);
//                    }
//                });

    }

    public class LocationListenerImp implements LocationListener {
        private City mCity;
        private LocationDetailsListener mLocationDetailsListener;
        private ResponseLocationNotFound mResponseLocationNotFound;


        public LocationListenerImp(City aCity, LocationDetailsListener aLocationDetailsListener,
                                   ResponseLocationNotFound aResponseLocationNotFound) {
            mCity = aCity;
            mLocationDetailsListener = aLocationDetailsListener;
            mResponseLocationNotFound = aResponseLocationNotFound;
            Log.d("AAA_network", "LocationListenerImp const !!! + mResponseLocationNotFound = " + String.valueOf(mResponseLocationNotFound));

        }

        @Override
        public void doOnLocationResponse(JSONObject response) {
            // if not this place show not information in fragment 1
            //showNotFoundPlace

            Log.d("AAA_network", "doOnLocationResponse !!!");
            try {
                JSONArray arraySuggestions = response.getJSONArray("location_suggestions");
//            String countryName = response.getString("country_name");
//            //TODO if country Name not like in request stop
//            String entity_type = response.getString("entity_type");
//            String entity_id = response.getString("entity_id");
//            CityLocation location = new CityLocation(entity_type, entity_id);
                //  sendRequestLocationDetails(null, mLocationDetailsListener);
                checkLocation(response);

            } catch (JSONException aE) {
                Log.d("AAA_network", "doOnLocationResponse JSONException aE = " + aE.getMessage());
                aE.printStackTrace();
            }


        }


        @Override
        public void onResponse(JSONObject response) {
            Log.d("AAA_network", "onResponse response = " + response);
            doOnLocationResponse(response);
        }

        @Override
        public void onError(ANError anError) {
            String body = anError.getErrorBody();
            Log.d("AAA_network", "onError error = " + body);
        }

        private void checkLocation(JSONObject response) {
//            {
//                "location_suggestions": [
//                {
//                    "entity_type": "city",
//                        "entity_id": 280,
//                        "title": "New York City, New York State",
//                        "latitude": 40.71463,
//                        "longitude": -74.005806,
//                        "city_id": 280,
//                        "city_name": "New York City",
//                        "country_id": 216,
//                        "country_name": "United States"
//                }
//  ],
//                "status": "success",
//                    "has_more": 0,
//                    "has_total": 0
//            }
//
            try {
                JSONArray arraySuggestions = response.getJSONArray("location_suggestions");

                boolean isFoundLocation = false;
                for (int i = 0; i < arraySuggestions.length(); i++) {
                    JSONObject jsonObject = (JSONObject) arraySuggestions.get(i);
                    String title = jsonObject.getString("title");
                    Log.d("AAA_network", "checkLocation title = " + title);

                    double lat = jsonObject.getDouble("latitude");
                    double lng = jsonObject.getDouble("longitude");

                    float[] results = new float[1];
                    Location.distanceBetween(lat, lng,
                            mCity.getLat(), mCity.getLng(),
                            results);
                    Log.d("AAA_network", "checkLocation results[0] = " + results[0]);
                    if (results[0] < MAX_RADIUS) {
                        isFoundLocation = true;
                        String entity_type = jsonObject.getString("entity_type");
                        String entity_id = jsonObject.getString("entity_id");
                        CityLocation location = new CityLocation(entity_id, entity_type);
                        sendRequestLocationDetails(location, mLocationDetailsListener);
                        break;
                    }

                }
                if (!isFoundLocation) {
                    mResponseLocationNotFound.showNotFoundLocation(mCity);
                }


//            //TODO if country Name not like in request stop
//            String entity_type = response.getString("entity_type");
//            String entity_id = response.getString("entity_id");
//            CityLocation location = new CityLocation(entity_type, entity_id);
            } catch (JSONException aE) {
                Log.d("AAA_network", "checkLocation JSONException  " + aE.getMessage());
                aE.printStackTrace();
            }
//


        }
    }
}

