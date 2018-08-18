package com.example.angelika.beerapp.app;

import android.app.Application;

/**
 * Created by Angelika on 02.08.2018.
 */

public class BeerApplication extends Application {
    private static BeerApplication APP;

    @Override
    public void onCreate() {
        super.onCreate();
        APP = this;
    }

    public static BeerApplication getApp() {
        return APP;
    }


}
