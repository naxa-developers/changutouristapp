package com.naxa.np.changunarayantouristapp.placedetailsview;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

public class PlaceDetailsActivity extends BaseActivity {

    ImageView ivImageMain;
    TextView tvPlaceTitle, tvPlaceDesc;
    ImageButton btnView360Image;
    RatingBar ratingBar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        setupToolbar("Place Details", false);

        initUI();
    }

    private void initUI() {
        tvPlaceTitle = findViewById(R.id.tv_place_title);
        tvPlaceDesc = findViewById(R.id.tv_place_desc);
        ivImageMain = findViewById(R.id.iv_place_details_main);
        ratingBar = findViewById(R.id.rating_bar_place);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnView360Image = findViewById(R.id.btn_view_vr_image);
        btnView360Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.openActivity(VRImageViewActivity.class, PlaceDetailsActivity.this);
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_images:
                    toolbar.setTitle("Shop");
                    return true;
                case R.id.action_bottom_videos:
                    toolbar.setTitle("My Gifts");
                    return true;
                case R.id.action_bottom_audios:
                    toolbar.setTitle("Cart");
                    return true;
                case R.id.action_bottom_map:
                    ActivityUtil.openActivity(MapMainActivity.class, PlaceDetailsActivity.this);
                    return true;
            }
            return false;
        }
    };
}
