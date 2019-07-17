package com.naxa.np.changunarayantouristapp.placedetailsview.nearbyplaces;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.gps.GeoPointActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.PlaceDetailsActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlacesListViewHolder;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

                    if (!split_lat.equals("") && !split_lon.equals("")) {
                        myLat = Double.parseDouble(split_lat);
                        myLong = Double.parseDouble(split_lon);
                        showLoading("Please wait ... \nCalculating distance");
                        if(placesDetailsEntityList != null){

                            sortNearbyPlaces(placesDetailsEntityList);
                        }
                    } else {
//                        showInfoToast("Cannot calculate distance");
                        Toast.makeText(this, "Cannot get location", Toast.LENGTH_SHORT).show();

                        if(placesDetailsEntityList != null){
                            setUpRecyclerView(placesDetailsEntityList);
                        }

                    }

                    break;
            }
        }
    }

    private void sortNearbyPlaces(List<PlacesDetailsEntity> placesDetailsEntityList) {

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



    Map<PlacesDetailsEntity, Float> hashMapWithDistance;
    public void sortingNearByPlacesListData() {

        hashMapWithDistance = new HashMap<PlacesDetailsEntity, Float>();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (servicesData.size() > 1) {
////                    sortServiceData(servicesData, myLat, myLon);
//                    for (int i = 0; i < servicesData.size(); i++) {
//                        double latfirst = Double.parseDouble(servicesData.get(i).getServiceLat());
//                        double longfirst = Double.parseDouble(servicesData.get(i).getServiceLon());
//
//                        float[] result1 = new float[3];
//                        Location.distanceBetween(myLat, myLong, latfirst, longfirst, result1);
//                        Float distance1 = result1[0];
//
//                        hashMapWithDistance.put(servicesData.get(i), distance1);
//                    }
//                    sortMapByValuesWithDuplicates(hashMapWithDistance);
//                }
//            }
//        }).start();
    }

//    private void sortMapByValuesWithDuplicates(@NotNull Map passedMap) {
//        List mapKeys = new ArrayList(passedMap.keySet());
//        List mapValues = new ArrayList(passedMap.values());
//        Collections.sort(mapValues);
////        Collections.sort(mapKeys);
//
//        LinkedHashMap sortedMap = new LinkedHashMap();
//
//        Iterator valueIt = mapValues.iterator();
//        while (valueIt.hasNext()) {
//            Object val = valueIt.next();
//            Iterator keyIt = mapKeys.iterator();
//
//            while (keyIt.hasNext()) {
//                Object key = keyIt.next();
//                Object comp1 = passedMap.get(key);
//                Float comp2 = Float.parseFloat(val.toString());
//
//                if (comp1.equals(comp2)) {
//                    passedMap.remove(key);
//                    mapKeys.remove(key);
//                    sortedMap.put((ServicesData) key, (Float) val);
//                    break;
//                }
//            }
//        }
//        //Getting Set of keys from HashMap
//        Set<ServicesData> keySet = sortedMap.keySet();
//        //Creating an ArrayList of keys by passing the keySet
//        sortedServicesList = new ArrayList<ServicesData>(keySet);
//
//
//        //Getting Collection of values from HashMap
//        Collection<Float> values = sortedMap.values();
//        //Creating an ArrayList of values
//        sortedServicesDistanceList = new ArrayList<Float>(values);
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                initServicesList(sortedServicesList, sortedServicesDistanceList);
//            }
//        });
//
//    }


}
