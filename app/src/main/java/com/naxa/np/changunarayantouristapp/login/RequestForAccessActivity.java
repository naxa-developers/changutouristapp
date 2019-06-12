package com.naxa.np.changunarayantouristapp.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.FieldValidatorUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RequestForAccessActivity extends BaseActivity implements View.OnClickListener {

    private EditText etFullName, etEmail;
    private AutoCompleteTextView etCountry;
    MaterialButton btnRegister;

    String gender = "Male", purpose_for_visit = "Religious Visit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_access);
        setupToolbar("Register", false);
        initUI();
    }

    private void initUI() {
        etFullName = findViewById(R.id.et_user_full_name);
        etEmail = findViewById(R.id.et_user_email);
        etCountry = findViewById(R.id.et_user_country);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        setupCountryAutoComplete();
    }

    private void setupCountryAutoComplete() {

        String[] arr = getResources().getStringArray(R.array.countries_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, arr);

        etCountry.setThreshold(1);
        etCountry.setAdapter(adapter);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rb_male:
                if (checked)
                    gender = "Male";
                break;

            case R.id.rb_female:
                if (checked)
                    gender = "Female";
                break;

            case R.id.rb_others:
                if (checked)
                    gender = "Others";
                break;
        }
    }

    public void onVisitRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.rb_religious:
                if (checked)
                    purpose_for_visit = "Religious Visit";
                    break;

            case R.id.rb_sight_seeing:
                if (checked)
                    purpose_for_visit = "Sight Seeing";
                    break;

            case R.id.rb_visit_others:
                if (checked)
                    purpose_for_visit = "Others";
                    break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                if(FieldValidatorUtils.validateEditText(etFullName) &&
                FieldValidatorUtils.validateEmailPattern(etEmail) &&
                FieldValidatorUtils.validateAutoCompleteText(etCountry)){


                    if(isNetworkAvailable()) {
                        registerUser();
                    }else {
                        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void registerUser() {
        apiInterface.getUserregistrationResponse(new UserLoginDetails(etFullName.getText().toString(), etEmail.getText().toString(), etCountry.getText().toString(),
                gender, purpose_for_visit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(Constant.Network.KEY_MAX_RETRY_COUNT)
                .subscribe(new DisposableObserver<UserLoginResponse>() {
                    @Override
                    public void onNext(UserLoginResponse userLoginResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
