package com.example.angelika.beerapp.app;

import android.app.Application;

/**
 * Created by Angelika on 02.08.2018.
 */

public class RestaurantsApplication extends Application {
    private static RestaurantsApplication APP;

    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
    }

    public static RestaurantsApplication getApp() {
        return APP;
    }


}
