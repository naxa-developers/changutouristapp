package com.naxa.np.changunarayantouristapp.mayormessage;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.login.LoginActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;

import java.io.File;

public class MayorMessageActivity extends BaseActivity {


    VideoView videoView ;
    Button btnSkipMayorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mayor_message);

        setupToolbar("Video Player", false);

        initUI();
    }

    private void initUI() {
        videoView = findViewById(R.id.video_view);
        btnSkipMayorMessage = findViewById(R.id.btn_skip_mayor_message);

        btnSkipMayorMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(videoView.isPlaying()){
                    videoView.stopPlayback();
                }

                ActivityUtil.openActivity(LoginActivity.class, MayorMessageActivity.this);
                finish();

            }
        });

    }

    private void playMayorVideo(String fileName) {


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
