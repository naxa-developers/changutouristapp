package com.naxa.np.changunarayantouristapp.videoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;

import java.io.File;
import java.util.HashMap;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;

public class VideoPlayerActivity extends BaseActivity {

    VideoView videoView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        setupToolbar("Video Player", false);

        getNetIntent(getIntent());
    }

    private void getNetIntent(Intent intent) {

        if(intent != null){
            videoView = findViewById(R.id.video_view);
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            String fileName = (String) hashMap.get(KEY_VALUE);

            setupToolbar(fileName, false);


            String folderPath = CreateAppMainFolderUtils.getAppMediaFolderName();
            Log.d("PlaceDetailsActivity", "downloaFile: full file path  "+folderPath+ File.separator +  fileName);
            File targetFile = new File(folderPath+ File.separator +  fileName);

            //Set MediaController  to enable play, pause, forward, etc options.
            MediaController mediaController= new MediaController(this);
            mediaController.setAnchorView(videoView);
            //Location of Media File
            Uri uri = Uri.fromFile(targetFile);
            //Starting VideView By Setting MediaController and URI
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);
            videoView.requestFocus();
            videoView.start();
        }

    }
}
