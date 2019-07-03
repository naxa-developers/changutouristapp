package com.naxa.np.changunarayantouristapp.mayormessage;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenter;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadView;
import com.naxa.np.changunarayantouristapp.login.LoginActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.IS_MAYOR_MESSAGE_FIRST_TIME;

public class MayorMessageActivity extends BaseActivity implements FileDownloadView {


    VideoView videoView;
    Button btnSkipMayorMessage;
    Gson gson;

    FileDownloadPresenter fileDownloadPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mayor_message);

        gson = new Gson();
        fileDownloadPresenter = new FileDownloadPresenterImpl(this, MayorMessageActivity.this);


        setupToolbar("Mayor Message", false);

        initUI();

        fetchMayorMessage();
    }

    Dialog dialog;
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
                            if (mayorMessagesListResponse == null) {
                                dialog = DialogFactory.createSimpleOkErrorDialog(MayorMessageActivity.this, "Data Fetch Error", "unable to download data ");
                                dialog.show();
                                return;
                            }

                            if (mayorMessagesListResponse.getError() == 0) {
                                dialog.dismiss();
                                if (mayorMessagesListResponse.getData() != null) {
                                    SharedPreferenceUtils.getInstance(MayorMessageActivity.this).setValue(Constant.SharedPrefKey.KEY_MAYOR_MESSAGE_DETAILS, gson.toJson(mayorMessagesListResponse));
                                    SharedPreferenceUtils.getInstance(MayorMessageActivity.this).setValue(IS_MAYOR_MESSAGE_FIRST_TIME, false);
                                    mayorMessageDetails = mayorMessagesListResponse.getData().get(0);

                                    downloadVideo(mayorMessageDetails);
                                }
                            } else {
                                dialog.dismiss();
                                dialog = DialogFactory.createSimpleOkErrorDialog(MayorMessageActivity.this, "Data Fetch Error", mayorMessagesListResponse.getMessage());
                                dialog.show();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog = DialogFactory.createSimpleOkErrorDialog(MayorMessageActivity.this, "Data Fetch Error", e.getMessage());
                            dialog.show();
                        }

                        @Override
                        public void onComplete() {
                            dialog.dismiss();
                        }
                    });
        } else {
            MayorMessagesListResponse mayorMessagesListResponse = gson.fromJson(SharedPreferenceUtils.getInstance(MayorMessageActivity.this).getStringValue(Constant.SharedPrefKey.KEY_MAYOR_MESSAGE_DETAILS, null), MayorMessagesListResponse.class);
            mayorMessageDetails = mayorMessagesListResponse.getData().get(0);
            downloadVideo(mayorMessageDetails);
        }

    }

    private void downloadVideo(@NotNull MayorMessageDetails mayorMessageDetails) {
        if (!TextUtils.isEmpty(mayorMessageDetails.getVideo())) {

            fileDownloadPresenter.handleFileDownload(mayorMessageDetails.getVideo(), mayorMessageDetails.getTitle(), CreateAppMainFolderUtils.getAppMediaFolderName());

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

                ActivityUtil.openActivity(LoginActivity.class, MayorMessageActivity.this);
                finish();

            }
        });

    }


    private void playMayorVideo(String fileName) {

        Log.d(TAG, "playMayorVideo: "+fileName);

        String folderPath = CreateAppMainFolderUtils.getAppMediaFolderName();
        Log.d("PlaceDetailsActivity", "downloaFile: full file path  " + folderPath + File.separator + fileName);
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
        playMayorVideo(fileName);
    }

    @Override
    public void fileDownloadFailed(String failedMsg) {

    }
}
