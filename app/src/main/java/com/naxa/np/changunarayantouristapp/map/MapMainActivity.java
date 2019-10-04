package com.naxa.np.changunarayantouristapp.map;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.franmontiel.localechanger.LocaleChanger;
import com.google.gson.Gson;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.BaseActivity;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;
import com.naxa.np.changunarayantouristapp.database.viewmodel.GeoJsonCategoryViewModel;
import com.naxa.np.changunarayantouristapp.database.viewmodel.PlaceDetailsEntityViewModel;
import com.naxa.np.changunarayantouristapp.events.LayerAddedSuccessEvent;
import com.naxa.np.changunarayantouristapp.events.MapDataLayerListCheckEvent;
import com.naxa.np.changunarayantouristapp.events.MarkerClickEvent;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.DrawGeoJsonOnMap;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.DrawMarkerOnMap;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.DrawRouteOnMap;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.MapDataLayerDialogCloseListen;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.mapdialogs.MapDialogs;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.MapboxBaseStyleUtils;
import com.naxa.np.changunarayantouristapp.placedetailsview.PlaceDetailsActivity;
import com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails.MainPlacesListActivity;
import com.naxa.np.changunarayantouristapp.utils.ActivityUtil;
import com.naxa.np.changunarayantouristapp.utils.Constant;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.GpsUtils;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.ToastUtils;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_OBJECT;
import static com.naxa.np.changunarayantouristapp.utils.Constant.KEY_VALUE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_CHANGUNARAYAN_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_MAIN_PLACE_TYPE;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_NAGARKOT_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.SharedPrefKey.KEY_SELECTED_APP_LANGUAGE;

public class MapMainActivity extends BaseActivity implements OnMapReadyCallback, PermissionsListener,
        LocationListener, View.OnClickListener, AdapterView.OnItemSelectedListener {


    private static final String TAG = "MapMainActivity";

    Button btnNavigation;
    boolean isBtnGetRoutePressed = false;


    ImageView ivSlidingLayoutIndicator;
    ImageButton btnMapLayerData;
    ImageButton btnMapLayerSwitch;
    ImageButton btnUsersCurrentLocation;

    private SlidingUpPanelLayout mLayout;
    TextView tvMarkerTitle, tvMarkerDesc, tvDistanceAndTime;
    ImageView ivMarkerPrimaryImage;
    Button btnGoThere, btnViewMarkerDetails, btnPlacesDetailsList, btnRouteToMap;
    CardView btnLayoutMapList;

    Gson gson;

    Spinner mapPlaceListSpinner;


    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapboxMap;

    GeoJsonCategoryViewModel geoJsonCategoryViewModel;
    PlaceDetailsEntityViewModel placeDetailsEntityViewModel;
    PlacesDetailsEntity placesDetailsEntity;
    boolean isFromMainPlaceList = true;


    String filename = "";
    String placeType = "";
    private boolean isMapFirstTime = false;
    private boolean isMapPlaceLayerFromDialog = false;


    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;

    private Location originLocation;

    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private NavigationMapRoute navigationMapRoute;
    SharedPreferenceUtils sharedPreferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoicGVhY2VuZXBhbCIsImEiOiJjajZhYzJ4ZmoxMWt4MzJsZ2NnMmpsejl4In0.rb2hYqaioM1-09E83J-SaA");
        setContentView(R.layout.activity_map_main);

        gson = new Gson();
        geoJsonCategoryViewModel = ViewModelProviders.of(this).get(GeoJsonCategoryViewModel.class);
        placeDetailsEntityViewModel = ViewModelProviders.of(this).get(PlaceDetailsEntityViewModel.class);

        getMapFilterLayerCategoryList();


        setupToolbar(getResources().getString(R.string.explore_changunarayan_area, "Changunarayan"), false);
        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {
        btnNavigation = findViewById(R.id.navigation);
        ivSlidingLayoutIndicator = findViewById(R.id.iv_sliding_layout_indicator);
        btnMapLayerData = findViewById(R.id.btnMapLayerData);
        btnMapLayerSwitch = findViewById(R.id.btnMapLayerSwitch);
        btnUsersCurrentLocation = findViewById(R.id.btn_users_current_location);
        btnGoThere = findViewById(R.id.btnGoThere);

        tvMarkerTitle = findViewById(R.id.tv_marker_title);
        tvMarkerDesc = findViewById(R.id.tv_marker_desc);
        tvDistanceAndTime = findViewById(R.id.tv_distance_and_time);
        ivMarkerPrimaryImage = findViewById(R.id.iv_marker_primary_image);
        btnViewMarkerDetails = findViewById(R.id.btn_view_marker_details);
        btnPlacesDetailsList = findViewById(R.id.btn_route_to_main_places_list);
        btnRouteToMap = findViewById(R.id.btn_route_to_map);
        btnLayoutMapList = findViewById(R.id.btn_layout);


        btnNavigation.setOnClickListener(this);
        btnGoThere.setOnClickListener(this);
        btnMapLayerData.setOnClickListener(this);
        btnMapLayerSwitch.setOnClickListener(this);
        btnPlacesDetailsList.setOnClickListener(this);
        btnUsersCurrentLocation.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        sharedPreferenceUtils = new SharedPreferenceUtils(MapMainActivity.this);

        setupBottomSlidingPanel();

        netIntent(getIntent());

    }

    List<GeoJsonCategoryListEntity> maincategoryList = new ArrayList<>();

    private void getMapFilterLayerCategoryList() {
        geoJsonCategoryViewModel.getGeoJsonSubCategorySlugByLanguage(SharedPreferenceUtils.getInstance(MapMainActivity.this).getStringValue(KEY_SELECTED_APP_LANGUAGE, null))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new DisposableSubscriber<List<GeoJsonCategoryListEntity>>() {
                    @Override
                    public void onNext(List<GeoJsonCategoryListEntity> geoJsonCategoryListEntities) {
                        Log.d(TAG, "onNext: "+gson.toJson(geoJsonCategoryListEntities));
                        maincategoryList = geoJsonCategoryListEntities;
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private void netIntent(Intent intent) {

        try {

            if (intent != null) {
                HashMap<String, Object> hashMap = (HashMap<String, Object>) intent.getSerializableExtra("map");
                isFromMainPlaceList = (boolean) hashMap.get(KEY_VALUE);
                placesDetailsEntity = (PlacesDetailsEntity) hashMap.get(KEY_OBJECT);

                if (placesDetailsEntity != null) {
                    setupToolbar(placesDetailsEntity.getName(), false);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    private void setupBottomSlidingPanel() {
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);

                switch (newState.toString()) {

                    case "COLLAPSED":
                        ivSlidingLayoutIndicator.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_white_24dp));
                        btnLayoutMapList.setVisibility(View.VISIBLE);
                        if (isBtnGetRoutePressed) {
                            btnLayoutMapList.setVisibility(View.GONE);
                        }

                        break;

                    case "DRAGGING":
                        ivSlidingLayoutIndicator.setBackground(getResources().getDrawable(R.drawable.ic_sliding_neutral_white_24dp));
                        btnLayoutMapList.setVisibility(View.GONE);
                        break;

                    case "EXPANDED":
                        ivSlidingLayoutIndicator.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp));
                        btnLayoutMapList.setVisibility(View.GONE);
                        break;
                }

            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

    }

    private Dialog setupMapOptionsDialog() {
        // launch new intent instead of loading fragment

        int MAP_PLACE_BOUNDARY_ID = sharedPreferenceUtils.getIntValue(MAP_OVERLAY_LAYER, -1);

        return DialogFactory.createBaseLayerDialog(MapMainActivity.this, new DialogFactory.CustomBaseLayerDialogListner() {
                    @Override
                    public void onStreetClick() {
                        mapView.setStyleUrl(getResources().getString(R.string.mapbox_style_mapbox_streets));
                        if (MAP_PLACE_BOUNDARY_ID == KEY_CHANGUNARAYAN_BOARDER) {
                            onChangunarayanBoarderClick();
                        } else if (MAP_PLACE_BOUNDARY_ID == KEY_NAGARKOT_BOARDER) {
                            onNagarkotBoarderClick();
                        }

                    }

                    @Override
                    public void onSatelliteClick() {
                        mapView.setStyleUrl(getResources().getString(R.string.mapbox_style_satellite_streets));
                        if (MAP_PLACE_BOUNDARY_ID == KEY_CHANGUNARAYAN_BOARDER) {
                            onChangunarayanBoarderClick();
                        } else if (MAP_PLACE_BOUNDARY_ID == KEY_NAGARKOT_BOARDER) {
                            onNagarkotBoarderClick();
                        }

                    }

                    @Override
                    public void onOpenStreetClick() {
                        if (MAP_PLACE_BOUNDARY_ID == KEY_CHANGUNARAYAN_BOARDER) {
                            onChangunarayanBoarderClick();
                        } else if (MAP_PLACE_BOUNDARY_ID == KEY_NAGARKOT_BOARDER) {
                            onNagarkotBoarderClick();
                        }

                    }

                    @Override
                    public void onChangunarayanBoarderClick() {

                        if (isMapPlaceLayerFromDialog) {
                            mapPlaceListSpinner.setSelection(0);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                filename = "changunarayan_boundary.geojson";
                                drawGeoJsonOnMap.readAndDrawGeoSonFileOnMap(filename, true, "");
                                removeLayerFromMap("nagarkot_boundary.geojson");
                                placeType = "changunarayan";
                                setupToolbar(getResources().getString(R.string.explore_changunarayan_area, "Changunarayan"), false);

                                mapboxMap.setMinZoomPreference(14.5);
                            }
                        }, 50);

                    }

                    @Override
                    public void onNagarkotBoarderClick() {

                        if (isMapPlaceLayerFromDialog) {
                            mapPlaceListSpinner.setSelection(1);
                        }

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                filename = "nagarkot_boundary.geojson";
                                drawGeoJsonOnMap.readAndDrawGeoSonFileOnMap(filename, true, "");
                                removeLayerFromMap("changunarayan_boundary.geojson");
                                placeType = "nagarkot";
                                setupToolbar(getResources().getString(R.string.explore_changunarayan_area, "Nagarkot"), false);
//                                plotDefaultMarkerOnMap(placeType);

                                mapboxMap.setMinZoomPreference(11.5);
                            }
                        }, 50);

                    }

                    private void removeLayerFromMap(String filename) {

                        if (filename != null) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    drawGeoJsonOnMap.readAndDrawGeoSonFileOnMap(filename, false, "");
                                }
                            }, 50);
                        }
                    }


                }
        );


    }

    private Dialog setupMapDataLayerDialog(boolean isFirstTime, List<GeoJsonCategoryListEntity> maincategoryList) {

        if (mapboxMap == null) {
            Toast.makeText(this, "Your map is not ready yet", Toast.LENGTH_SHORT).show();
        }
        MapDialogs mapDialogs = new MapDialogs();

        return mapDialogs.createMapDataLayerDialog(MapMainActivity.this, geoJsonCategoryViewModel, maincategoryList, isFirstTime, new MapDataLayerDialogCloseListen() {
                    @Override
                    public void onDialogClose() {
                        drawCategoryWiseMarkersOnMap();
                    }

                    @Override
                    public void isFirstTime() {
                        drawCategoryWiseMarkersOnMap();

                    }

                }
        );
    }


    private void drawCategoryWiseMarkersOnMap() {
        Observable.just(mapDataLayerListCheckedEventList)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(new Function<List<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent>, Iterable<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent>>() {
                    @Override
                    public Iterable<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent> apply(List<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent> mapDataLayerListCheckedEvents) throws Exception {
                        return mapDataLayerListCheckedEvents;
                    }
                })
                .map(new Function<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent, MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent>() {
                    @Override
                    public MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent apply(MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent mapDataLayerListCheckedEvent) throws Exception {
                        return mapDataLayerListCheckedEvent;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent>() {
                    @Override
                    public void onNext(MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent mapDataLayerListCheckedEvent) {
                        Log.d(TAG, "onNext: filter " + mapDataLayerListCheckedEvent.getMultiItemSectionModel().getData_value());
                        Log.d(TAG, "onNext: filter " + placeType);
                        if (mapDataLayerListCheckedEvent.getChecked()) {

                            placeDetailsEntityViewModel.getPlacesDetailsEntityBYPlaceAndCategoryType(placeType, mapDataLayerListCheckedEvent.getMultiItemSectionModel().getData_value(), SharedPreferenceUtils.getInstance(MapMainActivity.this).getStringValue(KEY_SELECTED_APP_LANGUAGE, null))
//                        placeDetailsEntityViewModel.getAllPlacesDetailsEntity()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new DisposableSubscriber<List<PlacesDetailsEntity>>() {
                                        @Override
                                        public void onNext(List<PlacesDetailsEntity> placesDetailsEntities) {
                                            if (placesDetailsEntities == null) {
                                                ToastUtils.showShortToast("No Data Found.");
                                            } else {
                                                try {

                                                    drawMarkerOnMap.AddListOfMarkerOnMap(placesDetailsEntities, placesDetailsEntities.get(0).getCategoryType());
                                                } catch (IndexOutOfBoundsException | NullPointerException e) {
                                                    e.printStackTrace();
                                                }
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

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    DrawGeoJsonOnMap drawGeoJsonOnMap;
    DrawMarkerOnMap drawMarkerOnMap;
    DrawRouteOnMap drawRouteOnMap;
    MapboxBaseStyleUtils mapboxBaseStyleUtils;

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        this.mapboxMap.setMaxZoomPreference(22.0);
//        setMapCameraPosition();
        enableLocationComponent();
        mapboxMap.getUiSettings().setCompassFadeFacingNorth(false);
        mapboxMap.getUiSettings().setCompassImage(getResources().getDrawable(R.drawable.direction_compas_icon));


        drawGeoJsonOnMap = new DrawGeoJsonOnMap(MapMainActivity.this, mapboxMap, mapView);
        drawMarkerOnMap = new DrawMarkerOnMap(MapMainActivity.this, mapboxMap, mapView);
        drawRouteOnMap = new DrawRouteOnMap(MapMainActivity.this, mapboxMap, mapView);


        isMapFirstTime = true;

        initSpinner();


        setupMapOptionsDialog().hide();

        setupMapDataLayerDialog(true, maincategoryList).hide();


        mapView.invalidate();
    }

    private void initSpinner() {
        mapPlaceListSpinner = findViewById(R.id.spinner_map_places);

        int MAP_PLACE_BOUNDARY_ID = sharedPreferenceUtils.getIntValue(MAP_OVERLAY_LAYER, -1);

        mapView.setStyleUrl(getResources().getString(R.string.mapbox_style_mapbox_streets));
//        mapView.setStyleUrl("mapbox://styles/peacenepal/cjypk1l6w56y81co32qklu983");

        if (MAP_PLACE_BOUNDARY_ID == KEY_CHANGUNARAYAN_BOARDER) {
            mapPlaceListSpinner.setSelection(0);
        } else if (MAP_PLACE_BOUNDARY_ID == KEY_NAGARKOT_BOARDER) {
            mapPlaceListSpinner.setSelection(1);
        } else {
            mapPlaceListSpinner.setSelection(0);
        }

        mapPlaceListSpinner.setOnItemSelectedListener(this);
    }

    public void setMapCameraPosition(LatLng location) {

        CameraPosition position = new CameraPosition.Builder()
                .target(location) // Sets the new camera position
                .zoom(20.0) // Sets the zoom
                .bearing(0) // Rotate the camera
                .tilt(15) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 2000);
        mapView.invalidate();
        isFromMainPlaceList = false;

//        enableLocationComponent();
    }


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponentOptions options = LocationComponentOptions.builder(this)
                    .trackingGesturesManagement(true)
                    .accuracyColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .build();

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(this, options);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);


            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.NONE_GPS);
            locationComponent.setRenderMode(RenderMode.COMPASS);


//            originLocation = locationComponent.getLocationEngine().getLastLocation();

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, "You need to provide location permission to show your current location", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationComponent();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    private void animateCameraPosition(Location location) {
        CameraPosition position = new CameraPosition.Builder()
//                .target(new LatLng(27.7033, 85.4324)) // Sets the new camera position
                .target(new LatLng(location)) // Sets the new camera position
                .zoom(11) // Sets the zoom
                .bearing(0) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        Log.d("MapBox", "animateCameraPosition: ");


        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 2000);
    }


    ArrayList<LatLng> points = null;
    boolean point = false;


    private LatLng selectedMarkerPosition = new LatLng(0.0, 0.0);

    public void generateRouteToGoThere(@NonNull LatLng latLngPoint) {

        destinationCoord = latLngPoint;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        try {

            originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
            originLocation = mapboxMap.getLocationComponent().getLocationEngine().getLastLocation();

            destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
            originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());

            if (originPosition == null) {
                return;
            }
            if (destinationPosition == null) {
                return;
            }
            drawRouteOnMap.getRoute(originPosition, destinationPosition, tvDistanceAndTime);
            btnNavigation.setVisibility(View.VISIBLE);
            tvDistanceAndTime.setVisibility(View.VISIBLE);
            btnLayoutMapList.setVisibility(View.GONE);

            mapboxMap.setMinZoomPreference(8.0
            );
            isBtnGetRoutePressed = true;
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        } catch (NullPointerException e) {
            Toast.makeText(this, getResources().getString(R.string.searching_current_location), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    List<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent> mapDataLayerListCheckedEventList = new ArrayList<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent>();

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onRVItemClick(@NotNull MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent itemClick) {
        String name = itemClick.getMultiItemSectionModel().getData_value();
        if (mapDataLayerListCheckedEventList.size() == 0) {
            mapDataLayerListCheckedEventList.add(itemClick);

        } else if (mapDataLayerListCheckedEventList.size() > 0) {
            boolean alreadyExist = false;
            int itemPosition = 0;
            for (int i = 0; i < mapDataLayerListCheckedEventList.size(); i++) {
                itemPosition = i;
                if (mapDataLayerListCheckedEventList.get(i).getMultiItemSectionModel().getData_key().equals(itemClick.getMultiItemSectionModel().getData_key())) {
                    alreadyExist = true;
                    break;

                } else {
                    alreadyExist = false;
                }
            }

            if (alreadyExist) {
                if (!itemClick.getChecked()) {
                    mapDataLayerListCheckedEventList.remove(itemPosition);
                    mapDataLayerListCheckedEventList.add(itemClick);
                    Log.d(TAG, "onRVItemClick: Item Removed");

                }
            }
            if (!alreadyExist) {
                if (itemClick.getChecked()) {
                    mapDataLayerListCheckedEventList.add(itemClick);
                    Log.d(TAG, "onRVItemClick: Item Added");
                }
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarkerItemClick(@NotNull MarkerClickEvent.MarkerItemClick itemClick) {


        selectedMarkerPosition = itemClick.getLocation();
        if (selectedMarkerPosition == null) {
            btnGoThere.setVisibility(View.GONE);
        } else {
            btnGoThere.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(itemClick.getMarkerProperties())) {
            PlacesDetailsEntity placesDetailsEntity = gson.fromJson(itemClick.getMarkerProperties(), PlacesDetailsEntity.class);

            tvMarkerTitle.setText(placesDetailsEntity.getName());
            tvMarkerDesc.setText(placesDetailsEntity.getDescription());

            if (!TextUtils.isEmpty(fetchPromaryImageFromList(placesDetailsEntity))) {
                LoadImageUtils.loadImageToViewFromSrc(ivMarkerPrimaryImage, fetchPromaryImageFromList(placesDetailsEntity));
            }


            btnViewMarkerDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(KEY_VALUE, false);
                    hashMap.put(KEY_OBJECT, placesDetailsEntity);
                    ActivityUtil.openActivity(PlaceDetailsActivity.class, MapMainActivity.this, hashMap, false);

                }
            });

        }

//        mLayout.setAnchorPoint(0.52f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

        drawRouteOnMap.removeRoute();

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

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected boolean gps_enabled, network_enabled;

    @Override
    public void onLocationChanged(Location location) {
        originLocation = location;
//        animateCameraPosition(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, getResources().getString(R.string.please_enable_gps_provider), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {

            case R.id.navigation:
                drawRouteOnMap.enableNavigationUiLauncher(MapMainActivity.this);
                btnNavigation.setVisibility(View.GONE);
                tvDistanceAndTime.setVisibility(View.GONE);
                isBtnGetRoutePressed = false;
                btnLayoutMapList.setVisibility(View.VISIBLE);
                break;

            case R.id.btnMapLayerData:
                if (mapboxMap == null) {
                    Toast.makeText(this, getResources().getString(R.string.map_is_not_ready_yet), Toast.LENGTH_SHORT).show();
                    return;
                }
                mapboxMap.clear();
                setupMapOptionsDialog();
                mapDataLayerListCheckedEventList.clear();
                setupMapDataLayerDialog(false, maincategoryList).show();
                break;

            case R.id.btnMapLayerSwitch:
                isMapPlaceLayerFromDialog = true;
                setupMapOptionsDialog().show();
                break;

            case R.id.btn_users_current_location:
                if (originLocation != null) {
                    animateCameraPosition(originLocation);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.searching_your_current_location), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_route_to_main_places_list:
                ActivityUtil.openActivity(MainPlacesListActivity.class, MapMainActivity.this);
                break;

            case R.id.btnGoThere:
                checkNetworkAndGpsStatus(selectedMarkerPosition);

                break;
        }

    }

    private void checkNetworkAndGpsStatus(LatLng selectedMarkerPosition) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    // turn on GPS
                    if (isGPSEnable) {
                        generateRouteToGoThere(selectedMarkerPosition);
                    } else {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.MapKey.GPS_REQUEST) {
                generateRouteToGoThere(selectedMarkerPosition);
            }
        }
    }


    private void plotDefaultMarkerOnMap(String placeType) {

        Log.d(TAG, "plotDefaultMarkerOnMap: " + isMapFirstTime);
        if (!isMapFirstTime || !isFromMainPlaceList) {
            return;
        }

        mapboxMap.clear();
        mapView.invalidate();

        Constant constant = new Constant();
        placeDetailsEntityViewModel.getNearByPlacesListByPlaceTypeAndNearByTypeList(placeType,
                constant.getDefaultPlacesTypeListToPlotMarker(),
                SharedPreferenceUtils.getInstance(MapMainActivity.this).getStringValue(KEY_SELECTED_APP_LANGUAGE, null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<PlacesDetailsEntity>, List<PlacesDetailsEntity>>() {
                    @Override
                    public List<PlacesDetailsEntity> apply(List<PlacesDetailsEntity> placesDetailsEntities) throws Exception {
                        return placesDetailsEntities;
                    }
                })
                .map(new Function<PlacesDetailsEntity, PlacesDetailsEntity>() {
                    @Override
                    public PlacesDetailsEntity apply(PlacesDetailsEntity placesDetailsEntity) throws Exception {
                        return placesDetailsEntity;
                    }
                })
                .subscribe(new DisposableSubscriber<PlacesDetailsEntity>() {
                    @Override
                    public void onNext(PlacesDetailsEntity placesDetailsEntity) {
                        if (placesDetailsEntity != null) {
                            drawMarkerOnMap.addSingleMarker(placesDetailsEntity.getCategoryType(), gson.toJson(placesDetailsEntity));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        isMapFirstTime = false;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            placeType = "changunarayan";
            SharedPreferenceUtils.getInstance(MapMainActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_CHANGUNARAYAN_BOARDER);
            SharedPreferenceUtils.getInstance(MapMainActivity.this).setValue(KEY_MAIN_PLACE_TYPE, "changunarayan");
            mapboxMap.setMinZoomPreference(14.5);
        } else if (position == 1) {
            placeType = "nagarkot";
            SharedPreferenceUtils.getInstance(MapMainActivity.this).setValue(MAP_OVERLAY_LAYER, KEY_NAGARKOT_BOARDER);
            SharedPreferenceUtils.getInstance(MapMainActivity.this).setValue(KEY_MAIN_PLACE_TYPE, "nagarkot");
            mapboxMap.setMinZoomPreference(11.5);
        }

        setupMapOptionsDialog().hide();

        isMapFirstTime = true;
        layerLoadSuccessCount++;
        if(layerLoadSuccessCount >=2){
            isFromMainPlaceList = true;
        }


        isMapPlaceLayerFromDialog = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }

int layerLoadSuccessCount = 0;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLayerAddedSuccessEvent(@NotNull LayerAddedSuccessEvent.LayerAddedSuccess layerAddedSuccess) {


        if (layerAddedSuccess.isAdded()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (!isFromMainPlaceList) {
                        mapboxMap.clear();
                        setMapCameraPosition(drawMarkerOnMap.addSingleMarker(placesDetailsEntity.getCategoryType(), gson.toJson(placesDetailsEntity)).getPosition());
                    layerLoadSuccessCount++;
                    } else {
                        plotDefaultMarkerOnMap(placeType);
                    }
                }
            }, 50);
        }
    }
}