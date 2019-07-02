package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.PlaceDetailsActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

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
    Gson gson;
    private BaseRecyclerViewAdapter<PlacesDetailsEntity, MainPlacesListViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_places_list);

        gson = new Gson();
        setupToolbar(getString(R.string.place_list), false);


        initUI();

    }

    private void initUI() {
        recyclerView = findViewById(R.id.rv_main_places_list);
        btnRouteToMap = findViewById(R.id.btn_route_to_map);
        btnRouteToPlaces = findViewById(R.id.btn_route_to_main_places_list);
        btnRouteToPlaces.setEnabled(false);
        btnRouteToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.openActivity(MapMainActivity.class, MainPlacesListActivity.this);
            }
        });


        MainPlaceListDetailsResponse mainPlaceListDetailsResponse = gson.fromJson(SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).getStringValue(Constant.SharedPrefKey.KEY_MAIN_PLACES_list_DETAILS, null), MainPlaceListDetailsResponse.class);
        List<PlacesDetailsEntity> placesDetailsEntities = mainPlaceListDetailsResponse.getData();

        if (placesDetailsEntities != null && placesDetailsEntities.size() > 0) {

            setUpRecyclerView(placesDetailsEntities);
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
                mainPlacesListViewHolder.bindView(placesDetailsEntity);
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
                return new MainPlacesListViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
    }
}
