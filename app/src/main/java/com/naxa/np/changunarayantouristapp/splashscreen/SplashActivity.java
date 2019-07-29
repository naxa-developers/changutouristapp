package com.naxa.np.changunarayantouristapp.splashscreen;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.mayormessage.MayorMessageActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.languageswitchutils.AppLocale;
import com.naxa.np.changunarayantouristapp.utils.languageswitchutils.MyContextWrapper;

import java.util.Locale;

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
                AppLocale.changeLocale();
                ActivityRecreationHelper.recreate(SplashActivity.this, true);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LocaleChanger.onConfigurationChanged();
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, AppLocale.changeLocale()));
    }
}
