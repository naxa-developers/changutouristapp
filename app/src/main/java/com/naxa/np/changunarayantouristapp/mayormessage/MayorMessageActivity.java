package com.naxa.np.changunarayantouristapp.mayormessage;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.franmontiel.localechanger.LocaleChanger;
import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenter;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadView;
import com.naxa.np.changunarayantouristapp.login.LoginActivity;
import com.naxa.np.changunarayantouristapp.login.UserLoginResponse;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_MAYOR_MESSAGE_FIRST_TIME;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_USER_ALREADY_LOGGED_IN;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_USER_LOGGED_IN_RESPONSE;

public class MayorMessageActivity extends BaseActivity implements FileDownloadView {


    VideoView videoView;
    Button btnSkipMayorMessage;
    Gson gson;
    Dialog dialog;

    FileDownloadPresenter fileDownloadPresenter;
    String appLanguage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mayor_message);

        gson = new Gson();
        fileDownloadPresenter = new FileDownloadPresenterImpl(this, MayorMessageActivity.this);
        appLanguage = SharedPreferenceUtils.getInstance(MayorMessageActivity.this).getStringValue(KEY_SELECTED_APP_LANGUAGE, null);

        setupToolbar(getResources().getString(R.string.welcome_to_changunarayan), false);

        initUI();

        fetchMayorMessage();
    }

    MayorMessageDetails mayorMessageDetails = new MayorMessageDetails();

    private void fetchMayorMessage() {
        if (SharedPreferenceUtils.getInstance(MayorMessageActivity.this).getBoolanValue(IS_MAYOR_MESSAGE_FIRST_TIME, true)) {
            dialog = DialogFactory.createProgressDialog(this, "Please wait!!!\nfetching data.");
            dialog.show();
            apiInterface.getMaoyorMessagesListDetails(Constant.Network.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<MayorMessagesListResponse>() {
                        @Override
                        public void onNext(MayorMessagesListResponse mayorMessagesListResponse) {
                            dialog.dismiss();
                            if (mayorMessagesListResponse == null) {
                                dialog = DialogFactory.createSimpleOkErrorDialog(MayorMessageActivity.this, "Data Fetch Error", "unable to download data ");
                                dialog.show();
                                return;
                            }

                            if (mayorMessagesListResponse.getError() == 0) {
                                if (mayorMessagesListResponse.getData() != null) {
                                    SharedPreferenceUtils.getInstance(MayorMessageActivity.this).setValue(Constant.SharedPrefKey.KEY_MAYOR_MESSAGE_DETAILS, gson.toJson(mayorMessagesListResponse));
                                    SharedPreferenceUtils.getInstance(MayorMessageActivity.this).setValue(IS_MAYOR_MESSAGE_FIRST_TIME, false);

                                    for (MayorMessageDetails mayorMessageDetails1 : mayorMessagesListResponse.getData()){
                                        if(TextUtils.equals(appLanguage, mayorMessageDetails1.getLanguage())){
                                            mayorMessageDetails = mayorMessageDetails1;
                                            downloadVideo(mayorMessageDetails);
                                        }
                                    }
//                                    mayorMessageDetails = mayorMessagesListResponse.getData().get(0);

                                }
                            } else {
                                dialog = DialogFactory.createSimpleOkErrorDialog(MayorMessageActivity.this, "Data Fetch Error", mayorMessagesListResponse.getMessage());
                                dialog.show();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();
                            dialog = DialogFactory.createSimpleOkErrorDialog(MayorMessageActivity.this, "Data Fetch Error", e.getMessage());
                            dialog.show();
                        }

                        @Override
                        public void onComplete() {
//                            dialog.dismiss();
                        }
                    });
        } else {
            MayorMessagesListResponse mayorMessagesListResponse = gson.fromJson(SharedPreferenceUtils.getInstance(MayorMessageActivity.this).getStringValue(Constant.SharedPrefKey.KEY_MAYOR_MESSAGE_DETAILS, null), MayorMessagesListResponse.class);
//            mayorMessageDetails = mayorMessagesListResponse.getData().get(0);
//            downloadVideo(mayorMessageDetails);
            for (MayorMessageDetails mayorMessageDetails1 : mayorMessagesListResponse.getData()){
                if(TextUtils.equals(appLanguage, mayorMessageDetails1.getLanguage())){
                    mayorMessageDetails = mayorMessageDetails1;
                    downloadVideo(mayorMessageDetails);
                }
            }
        }

    }

    private void downloadVideo(@NotNull MayorMessageDetails mayorMessageDetails) {
        if (!TextUtils.isEmpty(mayorMessageDetails.getVideo())) {
            dialog = DialogFactory.createProgressDialog(MayorMessageActivity.this, "Please wait!!! \nDownloading " + mayorMessageDetails.getTitle()+" video file");
            dialog.show();
            fileDownloadPresenter.handleFileDownload(mayorMessageDetails.getVideo(), mayorMessageDetails.getTitle()+mayorMessageDetails.getCreatedAt(), CreateAppMainFolderUtils.getAppMediaFolderName());

        }
    }

    private void initUI() {
        videoView = findViewById(R.id.video_view);
        btnSkipMayorMessage = findViewById(R.id.btn_skip_mayor_message);

        btnSkipMayorMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (videoView.isPlaying()) {
                    videoView.stopPlayback();
                }

                if(SharedPreferenceUtils.getInstance(MayorMessageActivity.this).getBoolanValue(IS_USER_ALREADY_LOGGED_IN, false)){

                    validateDate();

                    if(validateDate()) {
                        ActivityUtil.openActivity(MainActivity.class, MayorMessageActivity.this);
                        finish();
                    }else {
                        ActivityUtil.openActivity(LoginActivity.class, MayorMessageActivity.this);
                        finish();
                    }
                }else {
                    ActivityUtil.openActivity(LoginActivity.class, MayorMessageActivity.this);
                    finish();
                }

            }
        });

    }

    private boolean validateDate() {
        UserLoginResponse userLoginResponse = gson.fromJson(SharedPreferenceUtils.getInstance(MayorMessageActivity.this).getStringValue(KEY_USER_LOGGED_IN_RESPONSE, null), UserLoginResponse.class);
        if(TextUtils.isEmpty(userLoginResponse.getDate())){
            return false;
        }else {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date expiryDate = new Date();
            try {
                expiryDate = dateFormat.parse(userLoginResponse.getDate());
//                expiryDate = dateFormat.parse("2019-07-31");
                Date date = new Date();
                Date locaDate = dateFormat.parse(dateFormat.format(date));

                return compareDates(locaDate, expiryDate);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return  false;
            }

        }

    }

    private boolean  compareDates(@NotNull Date localDate, @NotNull Date expiryDate)
    {
        boolean isLocalTimeEqualsorSmaller = false;
        //CompareTo() Method
        int dateDifference = localDate.compareTo(expiryDate);

        if(dateDifference >= 0) {
            isLocalTimeEqualsorSmaller = false;
        }
        else {
            isLocalTimeEqualsorSmaller = true;
        }
        return isLocalTimeEqualsorSmaller;
    }


    private void playMayorVideo(String fileName) {

        Log.d(TAG, "playMayorVideo: "+fileName);

        String folderPath = CreateAppMainFolderUtils.getAppMediaFolderName();
        Log.d("playMayorVideo", "downloadedFile: full file path  " + folderPath + File.separator + fileName);
        File targetFile = new File(folderPath + File.separator + fileName);

        //Set MediaController  to enable play, pause, forward, etc options.
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        //Location of Media File
        Uri uri = Uri.fromFile(targetFile);
        //Starting VideView By Setting MediaController and URI
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

    }

    @Override
    public void fileDownloadProgress(int progress, int total, String successMsg) {


    }

    @Override
    public void fileDownloadSuccess(String fileName, String successMsg, boolean isAlreadyExists) {

       if(dialog!= null && dialog.isShowing()){
           dialog.dismiss();
       }

        playMayorVideo(fileName);
    }

    @Override
    public void fileDownloadFailed(String failedMsg) {
        if(dialog!= null && dialog.isShowing()){
            dialog.dismiss();

            dialog = DialogFactory.createSimpleOkErrorDialog(MayorMessageActivity.this, "Video Downloading Error", failedMsg);
            dialog.show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
