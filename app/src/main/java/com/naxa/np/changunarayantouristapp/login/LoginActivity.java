package com.naxa.np.changunarayantouristapp.login;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.FieldValidatorUtils;

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

                    ActivityUtil.openActivity(MainActivity.class, LoginActivity.this);
                }
                break;

            case R.id.btn_request_for_access:
                ActivityUtil.openActivity(RequestForAccessActivity.class, LoginActivity.this);
                break;
        }
    }
}
