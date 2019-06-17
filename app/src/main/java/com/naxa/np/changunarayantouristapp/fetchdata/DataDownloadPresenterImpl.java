package com.naxa.np.changunarayantouristapp.fetchdata;

import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;

public class DataDownloadPresenterImpl implements DataDownloadPresenter {

    DataDonwloadView dataDonwloadView;

    public DataDownloadPresenterImpl(DataDonwloadView dataDonwloadView) {
        this.dataDonwloadView = dataDonwloadView;
    }

    @Override
    public void handleDataDownload(NetworkApiInterface apiInterface, String apiKey, String language) {

    }
}
