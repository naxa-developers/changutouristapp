package com.naxa.np.changunarayantouristapp.fetchdata;

import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonListViewModel;
import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;

public class DataDownloadPresenterImpl implements DataDownloadPresenter {

    DataDonwloadView dataDonwloadView;
    GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    GeoJsonListViewModel geoJsonListViewModel;

    int[] totalCount = new int[1];
    int[] progress = new int[1];
    String[] geoJsonDisplayName = new String[1];

    public DataDownloadPresenterImpl(DataDonwloadView dataDonwloadView, GeoJsonListViewModel geoJsonListViewModel, GeoJsonCategoryViewModel geoJsonCategoryViewModel) {
        this.dataDonwloadView = dataDonwloadView;
        this.geoJsonCategoryViewModel = geoJsonCategoryViewModel;
        this.geoJsonListViewModel = geoJsonListViewModel;
    }

    @Override
    public void handleDataDownload(NetworkApiInterface apiInterface, String apiKey, String language) {
        dataDonwloadView.downloadSuccess("success");


    }
}
