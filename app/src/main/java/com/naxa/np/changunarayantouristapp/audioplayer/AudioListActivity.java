package com.naxa.np.changunarayantouristapp.audioplayer;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.franmontiel.localechanger.LocaleChanger;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenter;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadView;
import com.naxa.np.changunarayantouristapp.placedetailsview.FileNameAndUrlPojo;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.videoplayer.VideosAudiosListViewHolder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;

public class AudioListActivity extends BaseActivity implements FileDownloadView {

    PlacesDetailsEntity placesDetailsEntity;
    RecyclerView recyclerView;
    FileDownloadPresenter fileDownloadPresenter;
    Dialog dialog;
    TextView tvNoDataFound;

    private BaseRecyclerViewAdapter<FileNameAndUrlPojo, VideosAudiosListViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        fileDownloadPresenter = new FileDownloadPresenterImpl(this, AudioListActivity.this);


        setupToolbar("Audio", false);

        initUI(getIntent());
    }

    private void initUI(Intent intent) {
        recyclerView = findViewById(R.id.recycler_view_audios);
        tvNoDataFound = findViewById(R.id.tv_no_data_found);

        if (intent != null) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

            if (placesDetailsEntity != null) {
                setupToolbar(placesDetailsEntity.getName(), false);
                if (!TextUtils.isEmpty(placesDetailsEntity.getAudio())) {
                    fetchAudioList();
                }else {
                        tvNoDataFound.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    private void fetchAudioList() {

        List<FileNameAndUrlPojo> audios;
        audios = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(placesDetailsEntity.getAudio());


            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(TAG, "fetchAudioList: " + jsonArray.getString(i));
                int imageCount = i + 1;
                audios.add(new FileNameAndUrlPojo(placesDetailsEntity.getName() + " Audio " + imageCount, jsonArray.getString(i)));
            }
            setupRecyclerView(audios);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    FileNameAndUrlPojo fileNameAndUrlPojo1;

    private void setupRecyclerView(List<FileNameAndUrlPojo> audios) {
        fileNameAndUrlPojo1 = new FileNameAndUrlPojo();
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<FileNameAndUrlPojo, VideosAudiosListViewHolder>(audios, R.layout.item_text_view) {

            @Override
            public void viewBinded(VideosAudiosListViewHolder videosAudiosListViewHolder, FileNameAndUrlPojo fileNameAndUrlPojo, int position) {
                Log.d(TAG, "viewBinded: " + position);
                videosAudiosListViewHolder.bindView(fileNameAndUrlPojo);
                videosAudiosListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileNameAndUrlPojo1 = fileNameAndUrlPojo;
                        dialog = DialogFactory.createProgressDialog(AudioListActivity.this, "Please wait!!! \nDownloading audio file" + fileNameAndUrlPojo.getName());
                        dialog.show();
//                        fileDownloadPresenter.handleFileDownload("http://changu.naxa.com.np//assets//admin/SampleVideo_1280x720_1mb_(3).mp4", "Sample video file test");
                        fileDownloadPresenter.handleFileDownload(fileNameAndUrlPojo.getFileUrl(), fileNameAndUrlPojo.getName(), CreateAppMainFolderUtils.getAppMediaFolderName());

                    }
                });

            }

            @Override
            public VideosAudiosListViewHolder attachViewHolder(View view) {
                return new VideosAudiosListViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void fileDownloadProgress(int progress, int total, String successMsg) {

    }


    MediaPlayer mp;

    @Override
    public void fileDownloadSuccess(String fileName, String successMsg, boolean isAlreadyExists) {
        dialog.dismiss();
        Log.d(TAG, "fileDownloadSuccess: " + fileName + " , " + successMsg + " , " + isAlreadyExists);


        mp = new MediaPlayer();
//set up MediaPlayer
        try {
            mp.setDataSource(CreateAppMainFolderUtils.getAppMediaFolderName() + File.separator + fileName);
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DialogFactory dialogFactory = new DialogFactory();
        dialogFactory.createAudioPlayerDialog(AudioListActivity.this, fileNameAndUrlPojo1.getName(), new DialogFactory.AudioPlayerDialogListner() {
            @Override
            public void onAudioPlay() {
                if (!mp.isPlaying()) {
                    try {
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }            }

            @Override
            public void onAudioPause() {
                if (mp.isPlaying()) {
                    try {
                        mp.pause();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onAudioStop() {
                if (mp.isPlaying()) {
                    try {
                        mp.stop();
                        mp.reset();
                        mp.setDataSource(CreateAppMainFolderUtils.getAppMediaFolderName() + File.separator + fileName);
                        mp.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onDialogClose() {
                if (mp.isPlaying()) {
                    try {
                        mp.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).show();

    }

    @Override
    public void fileDownloadFailed(String failedMsg) {
        dialog.dismiss();
        Log.d(TAG, "fileDownloadFailed: " + failedMsg);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
