package com.example.angelika.beerapp.providers;

import android.location.Location;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.example.angelika.beerapp.model.City;
import com.example.angelika.beerapp.model.CityLocation;
import com.example.angelika.beerapp.model.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelika on 16.08.2018.
 */

public class RestaurantsProvider {
    private final String KEY = "885cdf0f3c2235a1a5792084ddc74a9b";
    private final String LOCATION = "https://developers.zomato.com/api/v2.1/locations";
    private final String LOCATION_DETAILS = "https://developers.zomato.com/api/v2.1/location_details";
    private static final int MAX_RADIUS = 10000;
    private static final String TAG = "RestaurantsProvider";


    public void requestRestaurantsByCity(City aCity,
                                         final OnLocationDetailsListener aOnLocationDetailsListener) {
        AndroidNetworking.get(LOCATION)
                .addQueryParameter("user-key", KEY)
                .addQueryParameter("query", aCity.getCityName())
                .addHeaders("user-key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new OnLocationListenerImp(aCity, aOnLocationDetailsListener));
    }


    private void sendRequestLocationDetails(CityLocation aLocation, OnLocationDetailsListener aListener) {
        AndroidNetworking.get(LOCATION_DETAILS)
                .addQueryParameter("entity_type", aLocation.getEntityType())
                .addQueryParameter("entity_id", aLocation.getEntityId())
                .addHeaders("user-key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(aListener);
    }

    public List<Restaurant> getRestaurants(JSONObject response) {
        return getListRestaurants(response);
    }

    private List<Restaurant> getListRestaurants(JSONObject response) {
        List<Restaurant> list = new ArrayList<>();
        try {
            JSONArray array = response.getJSONArray("best_rated_restaurant");
            for (int i = 0; i < array.length(); i++) {
                Restaurant restaurant = new Restaurant();
                JSONObject restaurantObject = ((JSONObject) array.get(i)).getJSONObject("restaurant");
                JSONObject location = restaurantObject.getJSONObject("location");

                String address = location.getString("address");
                String city = location.getString("city");
                address = address + ", " + city;
                restaurant.setAddress(address);

                String latitude = location.getString("latitude");
                String longitude = location.getString("longitude");
                Double lat = Double.valueOf(latitude);
                Double lng = Double.valueOf(longitude);

                restaurant.setLat(lat);
                restaurant.setLng(lng);

                String name = restaurantObject.getString("name");
                restaurant.setName(name);

                String cuisines = restaurantObject.getString("cuisines");
                restaurant.setCuisines(cuisines);

                int averageCostForTwo = restaurantObject.getInt("average_cost_for_two");
                restaurant.setAverageCostForTwo(String.valueOf(averageCostForTwo));

                String currency = restaurantObject.getString("currency");
                restaurant.setCurrency(currency);

                int priceRange = restaurantObject.getInt("price_range");
                restaurant.setPriceRange(String.valueOf(priceRange));

                String photosUrl = restaurantObject.getString("photos_url");
                restaurant.setPhotoURL(photosUrl);

                JSONObject userRating = restaurantObject.getJSONObject("user_rating");
                String rating = userRating.getString("aggregate_rating");
                restaurant.setUserRating(rating);

                list.add(restaurant);
            }

        } catch (JSONException aE) {
            Log.d(TAG, "getRestaurants JSONException = " + aE.getMessage());
            aE.printStackTrace();
        }

        Log.d(TAG, "getRestaurants list.size() = " + list.size());
        return list;

    }

    public class OnLocationListenerImp implements OnLocationListener {
        private City mCity;
        private OnLocationDetailsListener mOnLocationDetailsListener;


        public OnLocationListenerImp(City aCity, OnLocationDetailsListener aOnLocationDetailsListener) {
            mCity = aCity;
            mOnLocationDetailsListener = aOnLocationDetailsListener;

        }

        @Override
        public void onLocationResponse(JSONObject response) {
            checkLocation(response);
        }

        @Override
        public void onLocationError(ANError aANError) {
            String body = aANError.getErrorBody();
            Log.d(TAG, "onError error = " + body);
        }


        @Override
        public void onResponse(JSONObject response) {
            Log.d(TAG, "onResponse response = " + response);

            try {
                String status = response.getString("status");
                if(status.equals("success")) {
                    onLocationResponse(response);
                }else{
                    //do something
                }
            } catch (JSONException aE) {
                aE.printStackTrace();
            }

        }

        @Override
        public void onError(ANError anError) {
            onLocationError(anError);
        }

        private void checkLocation(JSONObject response) {
            try {
                JSONArray arraySuggestions = response.getJSONArray("location_suggestions");
                boolean isFoundLocation = false;
                for (int i = 0; i < arraySuggestions.length(); i++) {
                    JSONObject jsonObject = (JSONObject) arraySuggestions.get(i);

                    double lat = jsonObject.getDouble("latitude");
                    double lng = jsonObject.getDouble("longitude");

                    float[] results = new float[1];
                    Location.distanceBetween(lat, lng,
                            mCity.getLat(), mCity.getLng(),
                            results);
                    Log.d(TAG, "checkLocation results[0] = " + results[0]);
                    if (results[0] < MAX_RADIUS) {
                        isFoundLocation = true;
                        String entity_type = jsonObject.getString("entity_type");
                        String entity_id = jsonObject.getString("entity_id");
                        CityLocation location = new CityLocation(entity_id, entity_type);
                        sendRequestLocationDetails(location, mOnLocationDetailsListener);
                        break;
                    }
                }
                if (!isFoundLocation) {
                    mOnLocationDetailsListener.showNotFoundLocation(mCity);
                }
            } catch (JSONException aE) {
                Log.d(TAG, "checkLocation JSONException  " + aE.getMessage());
                aE.printStackTrace();
            }
        }
    }
}

