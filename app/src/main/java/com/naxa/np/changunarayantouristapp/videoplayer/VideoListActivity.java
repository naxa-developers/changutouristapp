package com.naxa.np.changunarayantouristapp.videoplayer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenter;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadPresenterImpl;
import com.naxa.np.changunarayantouristapp.filedownload.FileDownloadView;
import com.naxa.np.changunarayantouristapp.placedetailsview.FileNameAndUrlPojo;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;

public class VideoListActivity extends BaseActivity implements FileDownloadView {

    PlacesDetailsEntity placesDetailsEntity;
    RecyclerView recyclerView;
    FileDownloadPresenter fileDownloadPresenter;
    Dialog dialog;
    TextView tvNoDataFound;

    private BaseRecyclerViewAdapter<FileNameAndUrlPojo, VideosAudiosListViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        fileDownloadPresenter = new FileDownloadPresenterImpl(this, VideoListActivity.this);


        setupToolbar("Images", false);

        initUI(getIntent());
    }

    private void initUI(Intent intent) {
        recyclerView = findViewById(R.id.recycler_view_videos);
        tvNoDataFound = findViewById(R.id.tv_no_data_found);

        if (intent != null) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

            if (placesDetailsEntity != null) {
                setupToolbar(placesDetailsEntity.getName(), false);
                if(!TextUtils.isEmpty(placesDetailsEntity.getVideos())) {
                    fetchVideosList();
                }else {
                    tvNoDataFound.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void fetchVideosList() {

        List<FileNameAndUrlPojo> videos;
        videos = new ArrayList<>();


        try {
            JSONArray jsonArray = new JSONArray(placesDetailsEntity.getVideos());
            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(TAG, "fetchVideosList: " + jsonArray.getString(i));
                int imageCount = i + 1;
                videos.add(new FileNameAndUrlPojo(placesDetailsEntity.getName() + " Video " + imageCount, jsonArray.getString(i)));
            }
            setupRecyclerView(videos);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    FileNameAndUrlPojo fileNameAndUrlPojo1;
    private void setupRecyclerView(List<FileNameAndUrlPojo> videos) {
        fileNameAndUrlPojo1 = new FileNameAndUrlPojo();
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<FileNameAndUrlPojo, VideosAudiosListViewHolder>(videos, R.layout.item_text_view) {

            @Override
            public void viewBinded(VideosAudiosListViewHolder videosAudiosListViewHolder, FileNameAndUrlPojo fileNameAndUrlPojo, int position) {
                Log.d(TAG, "viewBinded: "+position);
                videosAudiosListViewHolder.bindView(fileNameAndUrlPojo);
                videosAudiosListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileNameAndUrlPojo1 = fileNameAndUrlPojo;

                        dialog = DialogFactory.createProgressDialog(VideoListActivity.this , "Please wait!!! \nDownloading video file"+fileNameAndUrlPojo.getName());
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

    @Override
    public void fileDownloadSuccess(String fileName, String successMsg, boolean isAlreadyExists) {
        dialog.dismiss();
        Log.d(TAG, "fileDownloadSuccess: "+fileName + " , "+ successMsg + " , "+isAlreadyExists);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(KEY_VALUE, fileName);
        hashMap.put(KEY_OBJECT, fileNameAndUrlPojo1);
        ActivityUtil.openActivity(VideoPlayerActivity.class, VideoListActivity.this, hashMap, false);
    }

    @Override
    public void fileDownloadFailed(String failedMsg) {
        dialog.dismiss();
        Log.d(TAG, "fileDownloadFailed: "+failedMsg);

    }
}
