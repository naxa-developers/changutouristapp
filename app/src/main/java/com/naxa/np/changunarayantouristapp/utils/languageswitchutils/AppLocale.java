package com.naxa.np.changunarayantouristapp.utils.languageswitchutils;

import android.text.TextUtils;

import com.franmontiel.localechanger.LocaleChanger;
import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.Locale;

public class AppLocale {

    public static Locale changeLocale() {
        String alias = "en";
        String appLanguage  = SharedPreferenceUtils.getInstance(ChangunarayanTouristApp.getInstance()).getStringValue(Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE, null);
        if(!TextUtils.isEmpty(appLanguage)){
            alias = appLanguage;
        }

        return changeAppLocale(alias);
    }

    private static Locale changeAppLocale(String alias) {
//        String language = "en";


        Locale newLocale ;
        newLocale = new Locale("en", "US");
        if(TextUtils.equals(alias, "nep")) {
            newLocale = new Locale("ne", "NP");
        }
        LocaleChanger.setLocale(newLocale);
        return newLocale;
    }
}
