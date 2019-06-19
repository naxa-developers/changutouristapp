package com.naxa.np.changunarayantouristapp.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.FieldValidatorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    MaterialButton btnVerification, btnRequestForAccess;

    EditText etVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupToolbar("Explore", false);

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
                    verifyUser(etVerificationCode.getText().toString());

                }
                break;

            case R.id.btn_request_for_access:
                ActivityUtil.openActivity(RequestForAccessActivity.class, LoginActivity.this);
                break;
        }
    }

    Dialog dialog;
    private void verifyUser(String verificatioCode) {
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

                                            ActivityUtil.openActivity(MainActivity.class, LoginActivity.this);

                                    dialog.show();
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

                        }
                    });

    } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
