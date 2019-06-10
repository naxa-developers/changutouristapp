package com.naxa.np.changunarayantouristapp.selectlanguage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;

public class SelectlanguageActivity extends BaseActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView tvSelectEnglish;
    TextView tvSelectNepali;
    TextView tvSelectChinese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectlanguage);

        setupToolbar("Select Language", false);
        initUI();

    }

    private void initUI(){
        toolbar = findViewById(R.id.toolbar);
        tvSelectEnglish = findViewById(R.id.tv_select_english);
        tvSelectEnglish.setOnClickListener(this);
        tvSelectNepali = findViewById(R.id.tv_select_nepali);
        tvSelectNepali.setOnClickListener(this);
        tvSelectChinese = findViewById(R.id.tv_select_chinese);
        tvSelectChinese.setOnClickListener(this);
    }




    private void launchHomeScreen() {
        ActivityUtil.openActivity(MainActivity.class, SelectlanguageActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_english:
                launchHomeScreen();
                break;
            case R.id.tv_select_nepali:
                launchHomeScreen();
                break;
            case R.id.tv_select_chinese:
                launchHomeScreen();
                break;
        }
    }
}
