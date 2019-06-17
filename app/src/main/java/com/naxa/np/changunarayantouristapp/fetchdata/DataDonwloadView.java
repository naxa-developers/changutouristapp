package com.naxa.np.changunarayantouristapp.fetchdata;

public interface DataDonwloadView {
    
    void downloadProgress ( int progress, int total, String successMsg);

    void downloadSuccess (String successMsg);

    void  downloadFailed (String failedMsg);
}
