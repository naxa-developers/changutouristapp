package com.naxa.np.changunarayantouristapp.splashscreen;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.mayormessage.MayorMessageActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_CHANGUNARAYAN_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Permission.STORAGE_READ;
import static com.naxa.np.changunarayantouristapp.utils.Constant.Permission.STORAGE_WRITE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.PermissionID.RC_STORAGE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_APP_FIRST_TIME_LAUNCH;

public class SplashActivity extends BaseActivity {

    CreateAppMainFolderUtils createAppMainFolderUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActivityFullScreen();
        setContentView(R.layout.activity_splash);


        checkStoragePermission();
    }


    private void makeActivityFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void checkStoragePermission() {

        checkPermission(RC_STORAGE, new String[]{STORAGE_READ, STORAGE_WRITE}, getString(R.string.storage_rationale),
                new BaseActivity.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {

                        createAppMainFolderUtils = new CreateAppMainFolderUtils(SplashActivity.this, CreateAppMainFolderUtils.appmainFolderName);
                        createAppMainFolderUtils.createMainFolder();

                        launchNextScreen();

                    }

                    @Override
                    public void onPermissionDenied() {
                        launchNextScreen();
                    }
                });
    }

    private void launchNextScreen() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                if (SharedPreferenceUtils.getInstance(SplashActivity.this).getBoolanValue(IS_APP_FIRST_TIME_LAUNCH, true)) {
                    SharedPreferenceUtils.getInstance(SplashActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_CHANGUNARAYAN_BOARDER);
                    ActivityUtil.openActivity(WalkThroughSliderActivity.class, SplashActivity.this);
                } else {
                    ActivityUtil.openActivity(MayorMessageActivity.class, SplashActivity.this);
                }
                finish();
            }

        }, 2000);
    }

}
