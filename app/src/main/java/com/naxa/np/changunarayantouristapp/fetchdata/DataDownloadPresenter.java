package com.naxa.np.changunarayantouristapp.fetchdata;

import com.naxa.np.changunarayantouristapp.network.NetworkApiInterface;

public interface DataDownloadPresenter {

    void handleDataDownload (NetworkApiInterface apiInterface, String apiKey, String language);

}
