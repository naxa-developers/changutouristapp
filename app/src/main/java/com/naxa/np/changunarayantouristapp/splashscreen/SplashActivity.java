package com.naxa.np.changunarayantouristapp.splashscreen;

import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.login.LoginActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlacesListActivity;
import com.naxa.np.changunarayantouristapp.selectlanguage.SelectlanguageActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_CHANGUNARAYAN_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_APP_FIRST_TIME_LAUNCH;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActivityFullScreen();
        setContentView(R.layout.activity_splash);

        new CreateAppMainFolderUtils(SplashActivity.this, CreateAppMainFolderUtils.appmainFolderName);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                if (SharedPreferenceUtils.getInstance(SplashActivity.this).getBoolanValue(IS_APP_FIRST_TIME_LAUNCH, true)) {
                    SharedPreferenceUtils.getInstance(SplashActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_CHANGUNARAYAN_BOARDER);
                    ActivityUtil.openActivity(WalkThroughSliderActivity.class, SplashActivity.this);
                } else {
                    ActivityUtil.openActivity(LoginActivity.class, SplashActivity.this);
                }
                finish();
            }

        }, 2000);
    }


    private void makeActivityFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
