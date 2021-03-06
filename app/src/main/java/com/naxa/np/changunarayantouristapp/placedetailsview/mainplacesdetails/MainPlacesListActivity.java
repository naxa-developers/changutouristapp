package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.franmontiel.localechanger.LocaleChanger;
import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.PlaceDetailsActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.GridSpacingItemDecorator;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_CHANGUNARAYAN_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_MAIN_PLACE_TYPE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_NAGARKOT_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;

public class MainPlacesListActivity extends BaseActivity {

    RecyclerView recyclerView;
    Button btnRouteToMap, btnRouteToPlaces;
    TextView tvNoDataFound;
    Gson gson;
    private BaseRecyclerViewAdapter<PlacesDetailsEntity, MainPlacesListViewHolder> adapter;
    List<PlacesDetailsEntity> placesDetailsEntityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_places_list);

        gson = new Gson();
        placesDetailsEntityList = new ArrayList<>();
        setupToolbar(getString(R.string.place_list), false);


        initUI();

    }

    private void initUI() {
        recyclerView = findViewById(R.id.rv_main_places_list);
        btnRouteToMap = findViewById(R.id.btn_route_to_map);
        btnRouteToPlaces = findViewById(R.id.btn_route_to_main_places_list);
        tvNoDataFound = findViewById(R.id.tv_no_data_found);
        btnRouteToPlaces.setEnabled(true);
        btnRouteToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.openActivity(MapMainActivity.class, MainPlacesListActivity.this);
            }
        });


        MainPlaceListDetailsResponse mainPlaceListDetailsResponse = gson.fromJson(SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).getStringValue(Constant.SharedPrefKey.KEY_MAIN_PLACES_list_DETAILS, null), MainPlaceListDetailsResponse.class);
        List<PlacesDetailsEntity> placesDetailsEntities = mainPlaceListDetailsResponse.getData();

        if (placesDetailsEntities != null && placesDetailsEntities.size() > 0) {

            for (PlacesDetailsEntity placesDetailsEntity  : placesDetailsEntities) {
                if (TextUtils.equals(placesDetailsEntity.getLanguage(), SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).getStringValue(Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE, null))) {

                    placesDetailsEntityList.add(placesDetailsEntity);
                }

            }
            if (placesDetailsEntityList.size() < 1) {
                tvNoDataFound.setVisibility(View.VISIBLE);
            } else {
                setUpRecyclerView(placesDetailsEntityList);
            }
        }


    }

    private void setUpRecyclerView(List<PlacesDetailsEntity> placesDetailsEntities) {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<PlacesDetailsEntity, MainPlacesListViewHolder>(placesDetailsEntities, R.layout.main_places_item_row_layout) {

            @Override
            public void viewBinded(MainPlacesListViewHolder mainPlacesListViewHolder, PlacesDetailsEntity placesDetailsEntity, int position) {
                Log.d(TAG, "viewBinded: " + position);
                mainPlacesListViewHolder.bindView(placesDetailsEntity, null);
                mainPlacesListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (placesDetailsEntity.getPlaceType().equals("changu")) {
                            SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_CHANGUNARAYAN_BOARDER);
                            SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).setValue(KEY_MAIN_PLACE_TYPE, "changunarayan");
                        } else {
                            SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_NAGARKOT_BOARDER);
                            SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).setValue(KEY_MAIN_PLACE_TYPE, "nagarkot");
                        }

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(KEY_VALUE, true);
                        hashMap.put(KEY_OBJECT, placesDetailsEntity);
                        ActivityUtil.openActivity(PlaceDetailsActivity.class, MainPlacesListActivity.this, hashMap, false);
                    }
                });

            }

            @Override
            public MainPlacesListViewHolder attachViewHolder(View view) {
                return new MainPlacesListViewHolder(view, getWindowManager().getDefaultDisplay(), true);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }


}
