package com.naxa.np.changunarayantouristapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.naxa.np.changunarayantouristapp.barcodereader.QRCodeReaderActivity;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonListViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDonwloadView;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDownloadPresenter;
import com.naxa.np.changunarayantouristapp.fetchdata.DataDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.NetworkUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

import static com.naxa.np.changunarayantouristapp.utils.Constant.Network.API_KEY;

public class MainActivity extends BaseActivity implements View.OnClickListener, DataDonwloadView {


    TextView tvQRScan, tvVRImage, tvViewOnMap;
    GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    GeoJsonListViewModel geoJsonListViewModel;
    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;
    ProgressDialog progressDialog;
    DataDownloadPresenter dataDownloadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        geoJsonCategoryViewModel = ViewModelProviders.of(this).get(GeoJsonCategoryViewModel.class);
        geoJsonListViewModel = ViewModelProviders.of(this).get(GeoJsonListViewModel.class);
        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);

        dataDownloadPresenter = new DataDownloadPresenterImpl(this, MainActivity.this, geoJsonListViewModel, geoJsonCategoryViewModel, placeDetailsEntityViewModel);


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
            if(NetworkUtils.isNetworkAvailable()) {
                fetctDataFromServerAndSave();
            }else {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                DialogFactory.createSimpleOkWithTitleDialog(MainActivity.this, getResources().getString(R.string.no_internet_connection),
                        getResources().getString(R.string.check_internet_retry_again), new DialogFactory.onClickListner() {
                            @Override
                            public void onClick() {
                                fetctDataFromServerAndSave();
                            }
                        }).show();
            }
                }


    }

    private void fetctDataFromServerAndSave() {
        progressDialog = DialogFactory.createProgressBarDialog(MainActivity.this, "", "");
        progressDialog.show();
        Log.d(TAG, "fetctDataFromServerAndSave: ");
        dataDownloadPresenter.handleDataDownload(apiInterface, API_KEY, SharedPreferenceUtils.getInstance(MainActivity.this).getStringValue(Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE, null));

    }


    @Override
    public void downloadProgress(int progress, int totalCount, String geoJsonFileName) {
        String alertMsg = getString(R.string.fetching_file, geoJsonFileName, String.valueOf(progress), String.valueOf(totalCount));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMax(totalCount);
                progressDialog.setMessage(alertMsg);
                progressDialog.setProgress(progress);
            }
        });

    }

    @Override
    public void downloadSuccess(String successMsg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void downloadFailed(String failedMsg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        DialogFactory.createSimpleOkErrorDialog(MainActivity.this, "Failed", failedMsg).show();
    }
}
