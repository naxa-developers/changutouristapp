package com.naxa.np.changunarayantouristapp.imageviewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import java.util.HashMap;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;

public class ImageListGridViewActivity extends BaseActivity {

    PlacesDetailsEntity placesDetailsEntity ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_grid_view);

        setupToolbar("Images", false);

        initUI(getIntent());
    }

    private void initUI(Intent intent) {
        if(intent != null){

            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

            if(placesDetailsEntity != null) {
                setupToolbar(placesDetailsEntity.getName(), false);

            }
        }
    }
}
