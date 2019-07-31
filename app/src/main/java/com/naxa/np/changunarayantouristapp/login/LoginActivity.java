package com.naxa.np.changunarayantouristapp.login;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.franmontiel.localechanger.LocaleChanger;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.FieldValidatorUtils;
import com.naxa.np.changunarayantouristapp.utils.NetworkUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_USER_ALREADY_LOGGED_IN;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_USER_LOGGED_IN_RESPONSE;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    MaterialButton btnVerification, btnRequestForAccess;

    EditText etVerificationCode;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupToolbar(getResources().getString(R.string.explore), false);

        initUI();
    }

    private void initUI() {
        etVerificationCode = findViewById(R.id.et_verification_code);
        btnVerification = findViewById(R.id.btn_verification);
        btnRequestForAccess = findViewById(R.id.btn_request_for_access);

        btnVerification.setOnClickListener(this);
        btnRequestForAccess.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verification:
                if(FieldValidatorUtils.validateEditText(etVerificationCode)){
                    if(NetworkUtils.isNetworkAvailable()) {
                        verifyUser(etVerificationCode.getText().toString());
                    }else {
                        dialog = DialogFactory.createSimpleOkErrorDialog(LoginActivity.this, "No Internet Connection!", "Please check your internet connection and try again.");
                        dialog.show();
                    }

                }
                break;

            case R.id.btn_request_for_access:
                ActivityUtil.openActivity(RequestForAccessActivity.class, LoginActivity.this);
                break;
        }
    }

    Dialog dialog;
    private void verifyUser(String verificatioCode) {
        Gson gson = new Gson();
        dialog = DialogFactory.createProgressDialog(LoginActivity.this, "Verifying User\nPlease wait....");
        dialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", verificatioCode);
            apiInterface.getUserVerificationResponse(Constant.Network.API_KEY, jsonObject.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<UserLoginResponse>() {
                        @Override
                        public void onNext(UserLoginResponse userLoginResponse) {
                            dialog.dismiss();
                            if (userLoginResponse == null) {
                                dialog = DialogFactory.createGenericErrorDialog(LoginActivity.this, "Verification failed");
                                dialog.show();
                            } else {
                                if (userLoginResponse.getError() == 0) {
                                    SharedPreferenceUtils.getInstance(LoginActivity.this).setValue(IS_USER_ALREADY_LOGGED_IN, true);
                                    SharedPreferenceUtils.getInstance(LoginActivity.this).setValue(KEY_USER_LOGGED_IN_RESPONSE, gson.toJson(userLoginResponse));

                                            ActivityUtil.openActivity(MainActivity.class, LoginActivity.this);
                                            finish();

                                } else {
                                    dialog = DialogFactory.createSimpleOkErrorDialog(LoginActivity.this, "Failed", userLoginResponse.getMessage());
                                    dialog.show();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            dialog = DialogFactory.createSimpleOkErrorDialog(LoginActivity.this, "Failed", e.getMessage());
                            dialog.show();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                        }
                    });

    } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
