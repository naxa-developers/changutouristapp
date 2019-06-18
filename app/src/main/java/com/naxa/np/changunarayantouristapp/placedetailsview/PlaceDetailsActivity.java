package com.naxa.np.changunarayantouristapp.placedetailsview;

import android.os.Bundle;


import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;

public class PlaceDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        setupToolbar("Place Details", false);
    }
}
