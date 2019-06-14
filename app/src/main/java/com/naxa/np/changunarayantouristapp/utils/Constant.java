package com.naxa.np.changunarayantouristapp.utils;


import android.Manifest;

public class Constant {
    public static final String KEY_OBJECT = "object";
    public static final String KEY_EXTRA_OBJECT = "extra_object";

    public class SharedPrefKey {
        public static final String IS_APP_FIRST_TIME_LAUNCH = "is_app_first_time_launch";

    }


    public class Network {
        public static final int KEY_MAX_RETRY_COUNT = 5;
    }

    public class PermissionID {
        public static final int RC_STORAGE = 1994;
        public static final int RC_CAMERA= 1995;
    }

    public class Permission {
        public static final String CAMERA = Manifest.permission.CAMERA;
        public static final String STORAGE_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        public static final String STORAGE_READ = Manifest.permission.READ_EXTERNAL_STORAGE;
    }
}
