package com.example.angelika.beerapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.example.angelika.beerapp.app.BeerApplication;
import com.example.angelika.beerapp.model.City;
import com.example.angelika.beerapp.model.Input;
import com.example.angelika.beerapp.model.Restaurant;
import com.example.angelika.beerapp.model.RestaurantInfo;
import com.example.angelika.beerapp.providers.LocationDetailsListener;
import com.example.angelika.beerapp.providers.ResponseLocationNotFound;
import com.example.angelika.beerapp.providers.RestaurantsProvider;
import com.example.angelika.beerapp.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ResponseLocationNotFound {
    // private final static int MAP_ZOOM  = 20;
    private final static int MAP_ZOOM = 10;
    private static final String TAG = "Restaurants";

    private GoogleMap mMap;
    private FragmentInfo mFragmentInfo;
    RestaurantsProvider mRestaurantsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mFragmentInfo = (FragmentInfo) getSupportFragmentManager()
                .findFragmentById(R.id.info_fragment);
        mapFragment.getMapAsync(this);

        AndroidNetworking.initialize(BeerApplication.getApp());
        mRestaurantsProvider = new RestaurantsProvider();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Tel Aviv and move the camera
        LatLng telAviv = new LatLng(32.07999147, 34.77001176);
        mMap.addMarker(new MarkerOptions().position(telAviv).title("Tel Aviv"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(telAviv));


    }

    private void moveMap(double aLat, double aLng) {
        LatLng latLng = new LatLng(aLat, aLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                MAP_ZOOM));
    }

    private void addMarkerRestaurant(Restaurant aRestaurant) {
        LatLng latLng = new LatLng(aRestaurant.getLat(), aRestaurant.getLng());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(aRestaurant.getName());

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(aRestaurant);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker aMarker) {
                if (aMarker.getTag() != null && aMarker.getTag() instanceof Restaurant) {
                    Restaurant restaurant = (Restaurant) aMarker.getTag();
                    showRestaurantInfo(restaurant);
                }
                return false;
            }
        });
    }

    private void addRestaurantsToMap(List<Restaurant> aList) {
        mMap.clear();
        for (Restaurant restaurant : aList) {
            addMarkerRestaurant(restaurant);
        }
    }


    private void showRestaurantInfo(Restaurant aRestaurant) {
        List<RestaurantInfo> list = Utils.getFieldsWithValueForInstanceRestaurant(Input.class, aRestaurant);
        mFragmentInfo.showRestaurantInfo(list);
    }

    private void showProgress(){
        mFragmentInfo.showProgress();
    }

    private void hideProgress(){
        mFragmentInfo.hideProgress();
    }


    public void sendRequest(City aCity) {
        mRestaurantsProvider.getLocationCity(aCity, this,
                new LocationDetailsListenerImp());
        showProgress();

    }



    @Override
    public void showNotFoundLocation(City aCity) {
        mFragmentInfo.displayNotFoundLocation(aCity);
        hideProgress();
    }


    public class LocationDetailsListenerImp implements LocationDetailsListener {

        @Override
        public void onResponse(JSONObject response) {
            Log.d("AAA_network", "onResponse DETAILS response = " + response);
            doOnLocationDetailsResponse(response);
            hideProgress();
        }

        @Override
        public void onError(ANError anError) {
            Log.d(TAG, "anError DETAILS body = " + anError.getErrorBody());
            hideProgress();
        }

        @Override
        public void doOnLocationDetailsResponse(JSONObject response) {
            moveToCityOnMap(response);
            List<Restaurant> listRestaurants = getRestaurants(response);
            addRestaurantsToMap(listRestaurants);
        }

        private void moveToCityOnMap(JSONObject response) {
            JSONObject location = null;
            try {
                location = response.getJSONObject("location");
                Double lat = location.getDouble("latitude");
                Double lng = location.getDouble("longitude");
                moveMap(lat, lng);
            } catch (JSONException aE) {
                aE.printStackTrace();
            }
        }

        private List<Restaurant> getRestaurants(JSONObject response) {
            List<Restaurant> listRestaurants = getListRestaurants(response);
            return listRestaurants;
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
    }
}
