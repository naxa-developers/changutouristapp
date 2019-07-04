package com.naxa.np.changunarayantouristapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
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
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlacesListActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.GpsUtils;
import com.naxa.np.changunarayantouristapp.utils.NetworkUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import static com.naxa.np.changunarayantouristapp.utils.Constant.Network.API_KEY;

public class MainActivity extends BaseActivity implements View.OnClickListener, DataDonwloadView, FileDownloadView, NavigationView.OnNavigationItemSelectedListener {


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
        setContentView(R.layout.main_activity_with_nav_drawer_layout);


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
        btnScanQR = findViewById(R.id.btn_qr_scan);
        btnViewOnMap = findViewById(R.id.btn_view_on_map);
        btnScanQR.setOnClickListener(this);
        btnViewOnMap.setOnClickListener(this);

        setupNavigationDrawer();
    }


    private void setupNavigationDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);


        View headerLayout = navigationView.getHeaderView(0);
        ImageView profileIageView = (ImageView) headerLayout.findViewById(R.id.nav_user_profile_image_view);
        TextView tvUserName = (TextView) headerLayout.findViewById(R.id.nav_user_username);
        TextView tvUserEmail = (TextView) headerLayout.findViewById(R.id.nav_user_email);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qr_scan:
                ActivityUtil.openActivity(QRCodeReaderActivity.class, MainActivity.this);
                break;

            case R.id.btn_view_on_map:

                new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        // turn on GPS
                        if(isGPSEnable){
                            ActivityUtil.openActivity(MapMainActivity.class, MainActivity.this);
                        }else {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }
                });

                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.MapKey.GPS_REQUEST) {
                ActivityUtil.openActivity(MapMainActivity.class, MainActivity.this);
            }
        }
    }


    private void fetchAllData() {
        if (SharedPreferenceUtils.getInstance(MainActivity.this).getBoolanValue(Constant.SharedPrefKey.IS_PLACES_DATA_ALREADY_EXISTS, false)) {

        } else {
            if (NetworkUtils.isNetworkAvailable()) {
                fetctDataFromServerAndSave();
            } else {
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
        switch (item.getItemId()) {
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

        String alertMsg = getString(R.string.fetching_file, geoJsonFileName, String.valueOf(progress), String.valueOf(totalCount));
        if (!TextUtils.isEmpty(markerImageUrl)) {
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
//        DialogFactory.createSimpleOkErrorDialog(MainActivity.this, "Failed", failedMsg).show();
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

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        switch (id) {
            case R.id.nav_home:
                break;

            case R.id.nav_qrcode_scanner:
                ActivityUtil.openActivity(QRCodeReaderActivity.class, this);
                break;

            case R.id.nav_map:
                ActivityUtil.openActivity(MapMainActivity.class, this);
                break;

            case R.id.nav_places_list:
                ActivityUtil.openActivity(MainPlacesListActivity.class, this);
                break;

            case R.id.nav_refresh_data:
                fetctDataFromServerAndSave();
                break;

            case R.id.nav_settings:
                break;

            case R.id.nav_about_us:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
