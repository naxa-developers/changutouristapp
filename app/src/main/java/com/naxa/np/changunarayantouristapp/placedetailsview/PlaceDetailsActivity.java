package com.naxa.np.changunarayantouristapp.placedetailsview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naxa.np.changunarayantouristapp.MainActivity;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.audioplayer.AudioListActivity;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.common.BaseRecyclerViewAdapter;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.imageviewer.ImageListGridViewActivity;
import com.naxa.np.changunarayantouristapp.map.MapMainActivity;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.SortByDistance;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlaceListDetails;
import com.naxa.np.changunarayantouristapp.placedetailsview.nearbyplaces.NearByPlacesListActivity;
import com.naxa.np.changunarayantouristapp.touristinformationguide.TourishInformationGuideActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.GpsUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;
import com.naxa.np.changunarayantouristapp.videoplayer.VideoListActivity;
import com.naxa.np.changunarayantouristapp.vrimage.VRImageViewActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_MAIN_PLACE_TYPE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE;

public class PlaceDetailsActivity extends BaseActivity implements View.OnClickListener, LocationListener {

    private static final String TAG = "PlaceDetailsActivity";
    ImageView ivImageMain;
    TextView tvPlaceTitle, tvPlaceDesc;
    ImageButton btnView360Image, btnTouristInfoGuide;
    Button btnViewAllNearByPlaces;
    RatingBar ratingBar;
    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerViewnearByPlaces;
    LinearLayout llNearByPlacesLayout;

    PlacesDetailsEntity placesDetailsEntity;
    boolean isFromMainPlaceList;
    String mainPlaceType = "";

    private List<PlacesDetailsEntity> sortedNearbyPlacesList;
    private List<Float> sortedNearbyPlacesDistanceList;
    List<PlacesDetailsEntity> placesDetailsEntityList = new ArrayList<>();
    private LocationManager locationManager;
    private String provider;
    Double Latitude, longitude;


    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;

    private BaseRecyclerViewAdapter<PlacesDetailsEntity, NearByPlacesViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);

        setupToolbar("Place Details", false);
        initUI();

        try {
            getnewIntent(getIntent());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

//        getUserLocation();
    }

    private void getnewIntent(Intent intent) {
        if (intent != null) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
            isFromMainPlaceList = (boolean) hashMap.get(KEY_VALUE);

            placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

            if (isFromMainPlaceList) {
                llNearByPlacesLayout.setVisibility(View.VISIBLE);
                mainPlaceType = SharedPreferenceUtils.getInstance(PlaceDetailsActivity.this).getStringValue(KEY_MAIN_PLACE_TYPE, null);
                initRecyclerView();
                bottomNavigationView.findViewById(R.id.action_bottom_videos).setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.getMenu().removeItem(R.id.action_bottom_videos);
            }

            if (placesDetailsEntity != null) {
                setValueOnView(placesDetailsEntity);
                setupToolbar(placesDetailsEntity.getName(), false);
            }

        }
    }

    private void initRecyclerView() {
        Constant constant = new Constant();
        placeDetailsEntityViewModel.getNearByPlacesListByPlaceTypeAndNearByTypeList(mainPlaceType,
                constant.getNearByPlacesTypeList(), SharedPreferenceUtils.getInstance(PlaceDetailsActivity.this).getStringValue(KEY_SELECTED_APP_LANGUAGE, null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<PlacesDetailsEntity>>() {
                    @Override
                    public void onNext(List<PlacesDetailsEntity> placesDetailsEntities) {
                        placesDetailsEntityList = placesDetailsEntities;
                        if (placesDetailsEntities != null && placesDetailsEntities.size() > 0) {
//                            setuprecyclerView(placesDetailsEntities);
                            getUserLocation();
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

    private void setValueOnView(@NotNull PlacesDetailsEntity placesDetailsEntity) {

        tvPlaceTitle.setText(placesDetailsEntity.getName());
        tvPlaceDesc.setText(placesDetailsEntity.getDescription());


//        if(isFromMainPlaceList){
//            if(!TextUtils.isEmpty(placesDetailsEntity.getPrimaryImage())){
//                LoadImageUtils.loadImageToViewFromSrc(ivImageMain, placesDetailsEntity.getPrimaryImage());
//            }
//        }else {

        if (!TextUtils.isEmpty(fetchPromaryImageFromList(placesDetailsEntity))) {
            LoadImageUtils.loadImageToViewFromSrc(ivImageMain, fetchPromaryImageFromList(placesDetailsEntity));
        }
//        }
    }

    private synchronized String fetchPromaryImageFromList(@NotNull PlacesDetailsEntity placesDetailsEntity) {
        String primaryImage = null;

        if (!TextUtils.isEmpty(placesDetailsEntity.getImages())) {
            try {
                JSONArray jsonArray = new JSONArray(placesDetailsEntity.getImages());
                if (jsonArray.length() > 0) {
                    primaryImage = jsonArray.optString(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return primaryImage;
    }

    private void setValueOnViewFromMainPlaces(@NotNull MainPlaceListDetails mainPlaceListDetails) {

        tvPlaceTitle.setText(mainPlaceListDetails.getTitle());
        tvPlaceDesc.setText(mainPlaceListDetails.getDescription());

        LoadImageUtils.loadImageToViewFromSrc(ivImageMain, mainPlaceListDetails.getImage());
    }

    private void initUI() {
        tvPlaceTitle = findViewById(R.id.tv_place_title);
        tvPlaceDesc = findViewById(R.id.tv_place_desc);
        ivImageMain = findViewById(R.id.iv_place_details_main);
        ratingBar = findViewById(R.id.rating_bar_place);
        recyclerViewnearByPlaces = findViewById(R.id.rv_nearby_places);
        llNearByPlacesLayout = findViewById(R.id.ll_nearby_places_layout);
        llNearByPlacesLayout.setVisibility(View.GONE);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        btnView360Image = findViewById(R.id.btn_view_vr_image);
        btnTouristInfoGuide = findViewById(R.id.btn_tourist_info_guide);
        btnViewAllNearByPlaces = findViewById(R.id.btn_view_all_nearby_places);
        btnViewAllNearByPlaces.setOnClickListener(this);
        btnView360Image.setOnClickListener(this);
        btnTouristInfoGuide.setOnClickListener(this);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_bottom_images:
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(ImageListGridViewActivity.class, PlaceDetailsActivity.this, hashMap, false);
                    return true;

                case R.id.action_bottom_videos:
                    HashMap<String, Object> hashMap1 = new HashMap<>();
                    hashMap1.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(VideoListActivity.class, PlaceDetailsActivity.this, hashMap1, false);
                    return true;

                case R.id.action_bottom_audios:
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(AudioListActivity.class, PlaceDetailsActivity.this, hashMap2, false);
                    return true;

                case R.id.action_bottom_map:
                    HashMap<String, Object> hashMap3 = new HashMap<>();
                    hashMap3.put(KEY_VALUE, isFromMainPlaceList);
                    hashMap3.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(MapMainActivity.class, PlaceDetailsActivity.this, hashMap3, false);
                    return true;
            }
            return false;
        }
    };


    private void setuprecyclerView(List<PlacesDetailsEntity> nearByPlacesPojoList) {

        if (nearByPlacesPojoList == null) {
            return;
        }

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewnearByPlaces.setLayoutManager(manager);
        recyclerViewnearByPlaces.setItemAnimator(new DefaultItemAnimator());
        adapter = new BaseRecyclerViewAdapter<PlacesDetailsEntity, NearByPlacesViewHolder>(nearByPlacesPojoList, R.layout.nearby_places_recycler_item_row_layout) {

            @Override
            public void viewBinded(NearByPlacesViewHolder nearByPlacesViewHolder, final PlacesDetailsEntity nearByPlacesPojo, int position) {
                nearByPlacesViewHolder.bindView(nearByPlacesPojo);
                nearByPlacesViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> hashMap3 = new HashMap<>();
                        hashMap3.put(KEY_VALUE, false);
                        hashMap3.put(KEY_OBJECT, nearByPlacesPojo);
                        ActivityUtil.openActivity(PlaceDetailsActivity.class, PlaceDetailsActivity.this, hashMap3, false);
                    }
                });

            }

            @Override
            public NearByPlacesViewHolder attachViewHolder(View view) {
                return new NearByPlacesViewHolder(view);
            }
        };
        recyclerViewnearByPlaces.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_view_vr_image:
                ActivityUtil.openActivity(VRImageViewActivity.class, PlaceDetailsActivity.this);
                break;

            case R.id.btn_view_all_nearby_places:
                new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                    @Override
                    public void gpsStatus(boolean isGPSEnable) {
                        // turn on GPS
                        if (isGPSEnable) {
                            ActivityUtil.openActivity(NearByPlacesListActivity.class, PlaceDetailsActivity.this);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }
                });

                break;

            case R.id.btn_tourist_info_guide:
                ActivityUtil.openActivity(TourishInformationGuideActivity.class, PlaceDetailsActivity.this);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.MapKey.GPS_REQUEST) {
                ActivityUtil.openActivity(NearByPlacesListActivity.class, PlaceDetailsActivity.this);
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {

            new Thread() {
                @Override
                public void run() {

                    LinkedHashMap sortedMap = new LinkedHashMap();

                    SortByDistance sortByDistance = new SortByDistance(location.getLatitude(), location.getLongitude());
                    sortedMap = sortByDistance.sortNearbyPlaces(placesDetailsEntityList);

                    //Getting Set of keys from HashMap
                    Set<PlacesDetailsEntity> keySet = sortedMap.keySet();
                    sortedNearbyPlacesList = new ArrayList<>(keySet);

                    //Getting Collection of values from HashMap
                    Collection<Float> values = sortedMap.values();
                    sortedNearbyPlacesDistanceList = new ArrayList<>(values);


                    if (sortedNearbyPlacesList != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setuprecyclerView(sortedNearbyPlacesList);

                            }
                        });
                    }

                }
            }.start();
        }
        else {
            setuprecyclerView(placesDetailsEntityList);

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void getUserLocation() {

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabledGPS = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (enabledGPS) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    onLocationChanged(null);
                    return;
                }
            }
            Location location = locationManager.getLastKnownLocation(provider);
            onLocationChanged(location);
        }else {
            onLocationChanged(null);
        }
    }


}
