package com.naxa.np.changunarayantouristapp.filedownload;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

public class FileDownloadPresenterImpl implements FileDownloadPresenter {
    private DownloadManager downloadManager;
    private long downloadId;
    private String fileType;
    private String downloadedFileName;

    FileDownloadView fileDownloadView;
    AppCompatActivity appCompatActivity;

    public FileDownloadPresenterImpl(FileDownloadView fileDownloadView, @NotNull AppCompatActivity appCompatActivity) {
        this.fileDownloadView = fileDownloadView;
        this.appCompatActivity = appCompatActivity;

    }


    @Override
    public void handleFileDownload(String fileSourceURL, String fileName, String folderPath) {

        downloaFile(fileSourceURL, fileName, folderPath);
    }


    private void downloaFile(String fileSourceURL, String fileName, String folderPath) {

        if (TextUtils.isEmpty(fileSourceURL)) {
            Toast.makeText(ChangunarayanTouristApp.getInstance(), "FIle Source URL Found", Toast.LENGTH_SHORT).show();
        } else {
            String fileUrl = fileSourceURL;
            int stringLength = fileUrl.length();
            fileType = fileUrl.substring(stringLength - 4, stringLength);
            downloadedFileName = fileName + fileType;

//            String folderPath = CreateAppMainFolderUtils.getAppMediaFolderName();
            Log.d("PlaceDetailsActivity", "downloaFile: full file path  "+folderPath+ File.separator +  downloadedFileName);
            File targetFile = new File(folderPath+ File.separator +  downloadedFileName);
            if (targetFile.exists()) {
                fileDownloadView.fileDownloadSuccess(downloadedFileName, "File already exist", true);

            } else {
                if (NetworkUtils.isNetworkAvailable()) {
                    //set filter to only when download is complete and register broadcast receiver
                    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                    appCompatActivity.registerReceiver(downloadReceiver, filter);
//                    ChangunarayanTouristApp.getInstance().registerReceiver(downloadReceiver, filter);


                    downloadId = DownloadData(fileSourceURL, fileName, folderPath);
                } else {
                    fileDownloadView.fileDownloadFailed("No internet connection");
                }
            }
        }
    }

    private long DownloadData(@NonNull String fileSourceURL, String fileName, String folderPath) {

        long downloadReference;
        DownloadManager.Request request = null;

        downloadManager = (DownloadManager) appCompatActivity.getSystemService(DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse(fileSourceURL));

        //Setting title of request
        request.setTitle(fileName);

        //Setting description of request
        request.setDescription("Changunarayan Tourist App");

        //Set the local destination for the downloaded file to a path within the application's external files directory

        if(TextUtils.equals(folderPath, CreateAppMainFolderUtils.getAppMapDataFolderName())){
            request.setDestinationInExternalPublicDir(CreateAppMainFolderUtils.appmainFolderName + "/" + CreateAppMainFolderUtils.mapFolderName, fileName + fileType);
        }else {
            request.setDestinationInExternalPublicDir(CreateAppMainFolderUtils.appmainFolderName + "/" + CreateAppMainFolderUtils.mediaFolderName, fileName + fileType);
        }

//        request.setDestinationInExternalPublicDir(folderPath, fileName + fileType);

//Enqueue download and save the referenceId
        downloadReference = downloadManager.enqueue(request);


        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, @NonNull Intent intent) {


            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId == downloadId) {
                Log.d("PlaceDetailsActivity", "onReceive: referenceID "+ referenceId);
                Log.d("PlaceDetailsActivity", "onReceive: downloadID "+ downloadId);
                fileDownloadView.fileDownloadSuccess(downloadedFileName, "File downloaded successfully", false);
            } else {
                fileDownloadView.fileDownloadFailed("Unable to download file");
            }

        }
    };

}
