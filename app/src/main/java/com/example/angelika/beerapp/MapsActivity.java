package com.example.angelika.beerapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.example.angelika.beerapp.model.City;
import com.example.angelika.beerapp.model.Input;
import com.example.angelika.beerapp.model.Restaurant;
import com.example.angelika.beerapp.model.RestaurantInfo;
import com.example.angelika.beerapp.providers.OnLocationDetailsListener;
import com.example.angelika.beerapp.providers.RestaurantsProvider;
import com.example.angelika.beerapp.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnLocationDetailsListener {

    private final static int MAP_ZOOM = 10;
    private static final String TAG = "Restaurants";

    private GoogleMap mMap;
    private FragmentInfo mFragmentInfo;
    private RestaurantsProvider mRestaurantsProvider;
    private City mCity;

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

        AndroidNetworking.initialize(this);
        mRestaurantsProvider = new RestaurantsProvider();

    }


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

    private void showProgress() {
        mFragmentInfo.showProgress();
    }

    private void hideProgress() {
        mFragmentInfo.hideProgress();
    }

    public void sendRequest(City aCity) {
        mCity = aCity;
        mRestaurantsProvider.requestRestaurantsByCity(aCity, this);
        showProgress();
    }

    private void moveToCityOnMap(City aCity) {
        Double lat = aCity.getLat();
        Double lng = aCity.getLng();
        moveMap(lat, lng);
    }

    @Override
    public void onLocationDetailsResponse(City aCity, List<Restaurant> aList) {
        moveToCityOnMap(aCity);
        addRestaurantsToMap(aList);
    }

    @Override
    public void showNotFoundLocation(City aCity) {
        mFragmentInfo.displayNotFoundLocation(aCity);
        hideProgress();
    }

    @Override
    public void onLocationDetailsError(ANError anError) {
        Log.d(TAG, "ANError anError : " + anError.getErrorBody());
        hideProgress();
    }

    @Override
    public void onResponse(JSONObject response) {
        List<Restaurant> listRestaurants = mRestaurantsProvider.getRestaurants(response);
        onLocationDetailsResponse(mCity, listRestaurants);
        hideProgress();
    }

    @Override
    public void onError(ANError anError) {
        onLocationDetailsError(anError);
    }

}
