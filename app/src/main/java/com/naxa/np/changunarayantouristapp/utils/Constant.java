package com.naxa.np.changunarayantouristapp.utils;


import android.Manifest;

public class Constant {
    public static final String KEY_OBJECT = "object";
    public static final String KEY_EXTRA_OBJECT = "extra_object";

    public class SharedPrefKey {
        public static final String IS_APP_FIRST_TIME_LAUNCH = "is_app_first_time_launch";

    }


    public class Network {
        public static final String API_KEY = "!@10293848576qwerprakadhkhdakatyuiozbsdfgprakashkhdkasdfgsdfhgpasdfghjklmnbvcxzkhadkapoiuytrewq!0_p_123456r_@a_@k_@a_@s_@h_aquickbrownfoxjumpoverthelazydog";
        public static final int KEY_MAX_RETRY_COUNT = 5;
    }

    public class PermissionID {
        public static final int RC_STORAGE = 1994;
        public static final int RC_CAMERA= 1995;
        public static final int RC_LOCATION= 1996;
    }

    public class Permission {
        public static final String CAMERA = Manifest.permission.CAMERA;
        public static final String STORAGE_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        public static final String STORAGE_READ = Manifest.permission.READ_EXTERNAL_STORAGE;
        public static final String LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION;
        public static final String LOCATION_COURSE = Manifest.permission.ACCESS_COARSE_LOCATION;
    }

    public class MapKey {
        public static final String MAP_BASE_LAYER = "base_layer";
        public static final String MAP_OVERLAY_LAYER = "overlay_layer";

        public static final int KEY_STREET = 0 ;
        public static final int KEY_SATELLITE = 1;
        public static final int KEY_OPENSTREET = 2;
        public static final int KEY_MUNICIPAL_BOARDER = 3;
        public static final int KEY_WARD = 4;
    }

}
