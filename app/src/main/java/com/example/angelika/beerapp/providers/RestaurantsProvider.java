package com.example.angelika.beerapp.providers;

import android.location.Location;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.angelika.beerapp.model.City;
import com.example.angelika.beerapp.model.CityLocation;
import com.example.angelika.beerapp.model.Input;
import com.example.angelika.beerapp.model.Restaurant;
import com.example.angelika.beerapp.model.RestaurantInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angelika on 16.08.2018.
 */

public class RestaurantsProvider {
    private static final String KEY = "885cdf0f3c2235a1a5792084ddc74a9b";
    private static final String LOCATION = "https://developers.zomato.com/api/v2.1/locations";
    private static final String LOCATION_DETAILS = "https://developers.zomato.com/api/v2.1/location_details";
    private static final int MAX_RADIUS = 10000;
    private static final String TAG = "RestaurantsProvider";

    private City mCity;
    private OnLocationDetailsListener mOnLocationDetailsListener;

    public void requestRestaurantsByCity(City aCity,
                                         final OnLocationDetailsListener aOnLocationDetailsListener) {

        mCity = aCity;
        mOnLocationDetailsListener = aOnLocationDetailsListener;

        AndroidNetworking.get(LOCATION)
                .addQueryParameter("user-key", KEY)
                .addQueryParameter("query", aCity.getCityName())
                .addHeaders("user-key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse response = " + response);

                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                checkLocation(response);
                            } else {
                                // TODO: show unknown error
                            }
                        } catch (JSONException aE) {
                            aE.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (mOnLocationDetailsListener != null) {
                            mOnLocationDetailsListener.onLocationDetailsError(anError);
                        }
                    }
                });
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
                    sendRequestLocationDetails(location);
                    break;
                }
            }
            if (!isFoundLocation && mOnLocationDetailsListener != null) {
                mOnLocationDetailsListener.onLocationNotFound(mCity);
            }
        } catch (JSONException aE) {
            Log.d(TAG, "checkLocation JSONException  " + aE.getMessage());
            aE.printStackTrace();
        }
    }

    private void sendRequestLocationDetails(CityLocation aLocation) {
        AndroidNetworking.get(LOCATION_DETAILS)
                .addQueryParameter("entity_type", aLocation.getEntityType())
                .addQueryParameter("entity_id", aLocation.getEntityId())
                .addHeaders("user-key", KEY)
                .setTag("test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Restaurant> listRestaurants = parseRestaurants(response);
                        if (mOnLocationDetailsListener != null) {
                            mOnLocationDetailsListener.onLocationDetailsSuccess(mCity, listRestaurants);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (mOnLocationDetailsListener != null) {
                            mOnLocationDetailsListener.onLocationDetailsError(anError);
                        }
                    }
                });
    }

    private List<Restaurant> parseRestaurants(JSONObject response) {
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

    public static List<RestaurantInfo> getFieldsWithValueForInstanceRestaurant(Class<? extends Annotation> ann, Restaurant inst) {
        List<RestaurantInfo> list = new ArrayList<>();
        Class c = Restaurant.class;
        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(ann)) {
                Annotation annotation = field.getAnnotation(Input.class);
                Input inputAnnotation = (Input) annotation;
                String name = inputAnnotation.name();
                int place = inputAnnotation.place();
                String methodName = "get" + field.getName().substring(1, field.getName().length());
                try {
                    //noinspection unchecked
                    Method method = c.getMethod(methodName);
                    try {
                        String value = (String) method.invoke(inst);
                        RestaurantInfo info = new RestaurantInfo(name + " : " + value, place);
                        list.add(info);
                    } catch (IllegalAccessException | InvocationTargetException aE) {
                        aE.printStackTrace();
                    }

                } catch (NoSuchMethodException aE) {
                    aE.printStackTrace();
                }
            }
        }

        return list;
    }
}

