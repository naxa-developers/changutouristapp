package com.naxa.np.changunarayantouristapp.common;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class ChangunarayanTouristApp  extends Application {

    private static Context instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }

    public static Context getInstance() {
        return instance;
    }
}
