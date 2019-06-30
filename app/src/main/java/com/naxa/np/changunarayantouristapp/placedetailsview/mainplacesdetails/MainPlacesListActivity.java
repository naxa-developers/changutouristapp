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
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_NAGARKOT_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;

public class MainPlacesListActivity extends BaseActivity {

    RecyclerView recyclerView;
    Button btnRouteToMap;
    Gson gson;
    private BaseRecyclerViewAdapter<MainPlaceListDetails, MainPlacesListViewHolder> adapter;


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
        btnRouteToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.openActivity(MapMainActivity.class, MainPlacesListActivity.this);
            }
        });


        MainPlaceListDetailsResponse mainPlaceListDetailsResponse = gson.fromJson(SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).getStringValue(Constant.SharedPrefKey.KEY_MAIN_PLACES_list_DETAILS, null), MainPlaceListDetailsResponse.class);
        List<MainPlaceListDetails> mainPlaceListDetailsList = mainPlaceListDetailsResponse.getData();

        if(mainPlaceListDetailsList != null && mainPlaceListDetailsList.size() >0){

            setUpRecyclerView(mainPlaceListDetailsList);
        }


    }

    private void setUpRecyclerView(List<MainPlaceListDetails> mainPlaceListDetailsList) {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new BaseRecyclerViewAdapter<MainPlaceListDetails, MainPlacesListViewHolder>(mainPlaceListDetailsList, R.layout.main_places_item_row_layout) {

            @Override
            public void viewBinded(MainPlacesListViewHolder mainPlacesListViewHolder, MainPlaceListDetails mainPlaceListDetails, int position) {
                Log.d(TAG, "viewBinded: " + position);
                mainPlacesListViewHolder.bindView(mainPlaceListDetails);
                mainPlacesListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
if(mainPlaceListDetails.getSlug().equals("changu")) {
    SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_CHANGUNARAYAN_BOARDER);
}else {
    SharedPreferenceUtils.getInstance(MainPlacesListActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_NAGARKOT_BOARDER);

}

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put(KEY_VALUE, true);
                        hashMap.put(KEY_OBJECT, mainPlaceListDetails);
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
