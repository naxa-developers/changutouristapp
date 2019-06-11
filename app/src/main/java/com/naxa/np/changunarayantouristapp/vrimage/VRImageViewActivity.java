package com.naxa.np.changunarayantouristapp.vrimage;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;

import java.io.InputStream;

public class VRImageViewActivity extends BaseActivity {

    private VrPanoramaView panoWidgetView;
    private VrPanoramaView mVRPanoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrimage_view);

        initUI();
    }

    private void initUI() {
//        panoWidgetView = findViewById(R.id.pano_view);
        mVRPanoramaView = findViewById(R.id.vrPanoramaView);
        loadPhotoSphere();
    }




    private void loadPhotoSphere() {
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        InputStream inputStream = null;

        AssetManager assetManager = getAssets();
        try {
            inputStream = assetManager.open("sunset_360.jpg");
            options.inputType = VrPanoramaView.Options.TYPE_MONO;
            mVRPanoramaView.loadImageFromBitmap(BitmapFactory.decodeStream(inputStream), options);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mVRPanoramaView.pauseRendering();
    }

    @Override
    public void onResume() {
        super.onResume();
        mVRPanoramaView.resumeRendering();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        super.onDestroy();
        mVRPanoramaView.shutdown();
    }
}


