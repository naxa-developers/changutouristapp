package com.naxa.np.changunarayantouristapp.placedetailsview.nearbyplaces;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.franmontiel.localechanger.LocaleChanger;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.gps.GeoPointActivity;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.SortByDistance;
import com.naxa.np.changunarayantouristapp.placedetailsview.PlaceDetailsActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlacesListViewHolder;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.naxa.np.changunarayantouristapp.gps.GeoPointActivity.LOCATION_RESULT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.GEOPOINT_RESULT_CODE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_MAIN_PLACE_TYPE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE;

public class NearByPlacesListActivity extends BaseActivity {

    private BaseRecyclerViewAdapter<PlacesDetailsEntity, MainPlacesListViewHolder> adapter;
    RecyclerView recyclerView;
    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;

    //    public static final String LOCATION_RESULT = "LOCATION_RESULT";
    double myLat = 0.0;
    double myLong = 0.0;
    private List<PlacesDetailsEntity> sortedNearbyPlacesList;
    private List<Float> sortedNearbyPlacesDistanceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places_list);
        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);


        setupToolbar(getString(R.string.nearby_places), false);

        initUI();


    }

    List<PlacesDetailsEntity> placesDetailsEntityList = new ArrayList<>();
    private void getCurrentLocation(List<PlacesDetailsEntity> placesDetailsEntities) {

        placesDetailsEntityList = placesDetailsEntities;
        Intent toGeoPointActivity = new Intent(this, GeoPointActivity.class);
        startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GEOPOINT_RESULT_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    String location = data.getStringExtra(LOCATION_RESULT);

                    Log.d(TAG, "onActivityResult: " + location.toString());

                    String string = location;
                    String[] parts = string.split(" ");
                    String split_lat = parts[0]; // 004
                    String split_lon = parts[1]; // 034556

                    if (!TextUtils.isEmpty(split_lat) && !TextUtils.isEmpty(split_lon)) {
                        myLat = Double.parseDouble(split_lat);
                        myLong = Double.parseDouble(split_lon);
                        showLoading("Please wait ... \nCalculating distance");
                        if(placesDetailsEntityList != null){
//                            sortNearbyPlaces(placesDetailsEntityList);

                            sortingNearByPlaces(placesDetailsEntityList, myLat, myLong);

                        }else {
                            Toast.makeText(this, "No nearby places found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
//                        showInfoToast("Cannot calculate distance");
                        Toast.makeText(this, "Cannot get location", Toast.LENGTH_SHORT).show();
                        if(placesDetailsEntityList != null){
                            setUpRecyclerView(placesDetailsEntityList, null);
                        }


                    }

                    break;

                case RESULT_CANCELED:
                    if(placesDetailsEntityList != null){
                        setUpRecyclerView(placesDetailsEntityList, null);
                    }
                    break;
            }
        }
    }


    private void initUI() {
        recyclerView = findViewById(R.id.rv_near_by_places_list);

        initRecyclerView(SharedPreferenceUtils.getInstance(NearByPlacesListActivity.this).getStringValue(KEY_MAIN_PLACE_TYPE, null));
    }

    private void initRecyclerView(String mainPlaceType) {

        if(mainPlaceType == null){
            return;
        }

        Constant constant = new Constant();
        placeDetailsEntityViewModel.getNearByPlacesListByPlaceTypeAndNearByTypeList(mainPlaceType,
                constant.getNearByPlacesTypeList(), SharedPreferenceUtils.getInstance(NearByPlacesListActivity.this).getStringValue(KEY_SELECTED_APP_LANGUAGE, null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<PlacesDetailsEntity>>() {
                    @Override
                    public void onNext(List<PlacesDetailsEntity> placesDetailsEntities) {
                        if(placesDetailsEntities != null && placesDetailsEntities.size()>0) {
                            getCurrentLocation(placesDetailsEntities);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setUpRecyclerView(List<PlacesDetailsEntity> placesDetailsEntities, List<Float> distanceList) {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new BaseRecyclerViewAdapter<PlacesDetailsEntity, MainPlacesListViewHolder>(placesDetailsEntities, R.layout.near_by_places_item_row_layout) {

            @Override
            public void viewBinded(MainPlacesListViewHolder mainPlacesListViewHolder, PlacesDetailsEntity placesDetailsEntity, int position) {
                Log.d(TAG, "viewBinded: " + position);
                if(distanceList != null){

                    mainPlacesListViewHolder.bindView(placesDetailsEntity, distanceList.get(position));
                }else {
                    mainPlacesListViewHolder.bindView(placesDetailsEntity, null);
                }
                mainPlacesListViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> hashMap3 = new HashMap<>();
                        hashMap3.put(KEY_VALUE, false);
                        hashMap3.put(KEY_OBJECT, placesDetailsEntity);
                        ActivityUtil.openActivity(PlaceDetailsActivity.class, NearByPlacesListActivity.this, hashMap3, false);

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
    

    public void sortingNearByPlaces(List<PlacesDetailsEntity> placesDetailsEntityList, double myLat, double myLong){
        LinkedHashMap sortedMap = new LinkedHashMap();

        SortByDistance sortByDistance = new SortByDistance(myLat, myLong);
        sortedMap = sortByDistance.sortNearbyPlaces(placesDetailsEntityList);

        //Getting Set of keys from HashMap
        Set<PlacesDetailsEntity> keySet = sortedMap.keySet();
        sortedNearbyPlacesList = new ArrayList<>(keySet);


        //Getting Collection of values from HashMap
        Collection<Float> values = sortedMap.values();
        sortedNearbyPlacesDistanceList = new ArrayList<>(values);

        if(sortedNearbyPlacesList != null) {
            setUpRecyclerView(sortedNearbyPlacesList, sortedNearbyPlacesDistanceList);
            hideLoading();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }

}
