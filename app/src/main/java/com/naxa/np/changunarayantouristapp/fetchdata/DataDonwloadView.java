package com.naxa.np.changunarayantouristapp.fetchdata;

public interface DataDonwloadView {
    
    void downloadProgress ( int progress, int total, String geoJsonFileName, String categoryName, String imageUrl);

    void downloadSuccess (String successMsg);

    void  downloadFailed (String failedMsg);
}
