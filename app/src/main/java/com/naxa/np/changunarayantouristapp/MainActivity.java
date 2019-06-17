package com.naxa.np.changunarayantouristapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.naxa.np.changunarayantouristapp.barcodereader.QRCodeReaderActivity;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    TextView tvQRScan, tvVRImage, tvViewOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar("Home Page", false);
        initUI();
    }

    private void initUI() {
        tvQRScan = findViewById(R.id.tv_qr_scanner);
        tvVRImage = findViewById(R.id.tv_view_vr_image);
        tvViewOnMap = findViewById(R.id.tv_view_map);

        tvQRScan.setOnClickListener(this);
        tvVRImage.setOnClickListener(this);
        tvViewOnMap.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_qr_scanner:
                ActivityUtil.openActivity(QRCodeReaderActivity.class, MainActivity.this);
                break;

            case R.id.tv_view_vr_image:
                ActivityUtil.openActivity(VRImageViewActivity.class, MainActivity.this);
                break;

            case R.id.tv_view_map:
                ActivityUtil.openActivity(MapMainActivity.class, MainActivity.this);

                break;


        }
    }
}
