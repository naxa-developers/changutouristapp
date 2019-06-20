package com.naxa.np.changunarayantouristapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.lifecycle.ViewModelProviders;

import com.naxa.np.changunarayantouristapp.barcodereader.QRCodeReaderActivity;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonListViewModel;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDonwloadView;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDownloadPresenter;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

import static com.naxa.np.changunarayantouristapp.utils.Constant.Network.API_KEY;

public class MainActivity extends BaseActivity implements View.OnClickListener, DataDonwloadView {

    TextView tvQRScan, tvVRImage, tvViewOnMap;
    GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    GeoJsonListViewModel geoJsonListViewModel;
    ProgressDialog progressDialog;
    DataDownloadPresenter dataDownloadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        geoJsonCategoryViewModel = ViewModelProviders.of(this).get(GeoJsonCategoryViewModel.class);
        geoJsonListViewModel = ViewModelProviders.of(this).get(GeoJsonListViewModel.class);

        dataDownloadPresenter = new DataDownloadPresenterImpl(this, geoJsonListViewModel, geoJsonCategoryViewModel);


        setupToolbar("Home Page", false);
        initUI();

        fetchAllData();


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

    private void fetchAllData() {
        if(SharedPreferenceUtils.getInstance(MainActivity.this).getBoolanValue(Constant.SharedPrefKey.IS_PLACES_DATA_ALREADY_EXISTS,false)){

        }else {
            fetchDataFromServerAndSave();
        }

    }

    private void fetchDataFromServerAndSave() {
        dataDownloadPresenter.handleDataDownload(apiInterface, API_KEY, SharedPreferenceUtils.getInstance(MainActivity.this).getStringValue(Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE, null));

    }

    @Override
    public void downloadProgress(int progress, int total, String successMsg) {

    }

    @Override
    public void downloadSuccess(String successMsg) {

    }

    @Override
    public void downloadFailed(String failedMsg) {

    }
}
