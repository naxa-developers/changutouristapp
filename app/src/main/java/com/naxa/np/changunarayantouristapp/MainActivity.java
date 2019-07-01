package com.naxa.np.changunarayantouristapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenter;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadView;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.NetworkUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

import static com.naxa.np.changunarayantouristapp.utils.Constant.Network.API_KEY;

public class MainActivity extends BaseActivity implements View.OnClickListener, DataDonwloadView, FileDownloadView  {


    TextView tvQRScan, tvVRImage, tvViewOnMap;
    ImageButton btnScanQR, btnViewOnMap;
    GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    GeoJsonListViewModel geoJsonListViewModel;
    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;
    ProgressDialog progressDialog;
    DataDownloadPresenter dataDownloadPresenter;
    FileDownloadPresenter fileDownloadPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        geoJsonCategoryViewModel = ViewModelProviders.of(this).get(GeoJsonCategoryViewModel.class);
        geoJsonListViewModel = ViewModelProviders.of(this).get(GeoJsonListViewModel.class);
        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);

        dataDownloadPresenter = new DataDownloadPresenterImpl(this, MainActivity.this, geoJsonListViewModel, geoJsonCategoryViewModel, placeDetailsEntityViewModel);
        fileDownloadPresenter = new FileDownloadPresenterImpl(this, MainActivity.this);


        setupToolbar("Home Page", false);
        initUI();


        fetchAllData();

    }



    private void initUI() {
        tvQRScan = findViewById(R.id.tv_qr_scanner);
        tvVRImage = findViewById(R.id.tv_view_vr_image);
        tvViewOnMap = findViewById(R.id.tv_view_map);

        btnScanQR = findViewById(R.id.btn_qr_scan);
        btnViewOnMap = findViewById(R.id.btn_view_on_map);
        btnScanQR.setOnClickListener(this);
        btnViewOnMap.setOnClickListener(this);

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

            case R.id.btn_qr_scan:
                ActivityUtil.openActivity(QRCodeReaderActivity.class, MainActivity.this);
                break;

            case R.id.btn_view_on_map:
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_select_language:
                break;
            case R.id.action_refresh_data:
                fetctDataFromServerAndSave();
                break;
        }
        return true;
    }

    private void fetctDataFromServerAndSave() {
        progressDialog = DialogFactory.createProgressBarDialog(MainActivity.this, "", "");
        progressDialog.show();
        Log.d(TAG, "fetctDataFromServerAndSave: ");
        dataDownloadPresenter.handleDataDownload(apiInterface, API_KEY, SharedPreferenceUtils.getInstance(MainActivity.this).getStringValue(Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE, null));

    }


    @Override
    public void downloadProgress(int progress, int totalCount, String geoJsonFileName, String categoryName, String markerImageUrl) {
        Log.d(TAG, "downloadProgress: "+"Progress : "+progress);
        Log.d(TAG, "downloadProgress: "+"GeoJsonFileName : "+geoJsonFileName);
        String alertMsg = getString(R.string.fetching_file, geoJsonFileName, String.valueOf(progress), String.valueOf(totalCount));
        if(!TextUtils.isEmpty(markerImageUrl)) {
            fileDownloadPresenter.handleFileDownload(markerImageUrl, categoryName, CreateAppMainFolderUtils.getAppMapDataFolderName());
        }
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



    @Override
    public void fileDownloadProgress(int progress, int total, String successMsg) {

    }

    @Override
    public void fileDownloadSuccess(String fileName, String successMsg, boolean isAlreadyExists) {

    }

    @Override
    public void fileDownloadFailed(String failedMsg) {

    }
}
