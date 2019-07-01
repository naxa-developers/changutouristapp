package com.naxa.np.changunarayantouristapp.placedetailsview.nearbyplaces;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.placedetailsview.NearByPlacesViewHolder;
import com.naxa.np.changunarayantouristapp.placedetailsview.PlaceDetailsActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlacesListActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlacesListViewHolder;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_CHANGUNARAYAN_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_MAIN_PLACE_TYPE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_NAGARKOT_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;

public class NearByPlacesListActivity extends BaseActivity {

    private BaseRecyclerViewAdapter<PlacesDetailsEntity, MainPlacesListViewHolder> adapter;
    RecyclerView recyclerView;
    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places_list);
        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);


        setupToolbar(getString(R.string.nearby_places), false);

        initUI();


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
                constant.getNearByPlacesTypeList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<PlacesDetailsEntity>>() {
                    @Override
                    public void onNext(List<PlacesDetailsEntity> placesDetailsEntities) {
                        if(placesDetailsEntities != null && placesDetailsEntities.size()>0) {
                            setUpRecyclerView(placesDetailsEntities);
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




}
