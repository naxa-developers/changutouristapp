package com.naxa.np.changunarayantouristapp.filedownload;

public interface FileDownloadView {

    void fileDownloadProgress ( int progress, int total, String successMsg);

    void fileDownloadSuccess (String fileName, String successMsg, boolean isAlreadyExists);

    void  fileDownloadFailed (String failedMsg);
}
