package com.naxa.np.changunarayantouristapp.common;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.facebook.stetho.Stetho;
import com.franmontiel.localechanger.LocaleChanger;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ChangunarayanTouristApp  extends Application {

    private static Context instance = null;

    public static final List<Locale> SUPPORTED_LOCALES =
            Arrays.asList(
                    new Locale("en", "US"),
                    new Locale("ne", "NP")
            );

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        instance = getApplicationContext();
        Stetho.initializeWithDefaults(this);

        LocaleChanger.initialize(getApplicationContext(), SUPPORTED_LOCALES);
    }

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
