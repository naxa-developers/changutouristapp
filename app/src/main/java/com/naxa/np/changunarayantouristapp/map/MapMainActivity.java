package com.naxa.np.changunarayantouristapp.map;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.naxa.np.changunarayantouristapp.events.MapDataLayerListCheckEvent;
import com.naxa.np.changunarayantouristapp.events.MarkerClickEvent;
import com.naxa.np.changunarayantouristapp.map.detailspage.MarkerDetailedDisplayAdapter;
import com.naxa.np.changunarayantouristapp.map.detailspage.MarkerDetailsKeyValue;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.DrawGeoJsonOnMap;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.DrawRouteOnMap;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.MapDataLayerDialogCloseListen;
import com.naxa.np.changunarayantouristapp.map.mapboxutils.MapboxBaseStyleUtils;
import com.naxa.np.changunarayantouristapp.utils.DialogFactory;
import com.naxa.np.changunarayantouristapp.utils.QueryBuildWithSplitter;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.DataServer;
import com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils.SectionMultipleItem;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_MUNICIPAL_BOARDER;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.KEY_WARD;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_OVERLAY_LAYER;

public class MapMainActivity extends BaseActivity implements OnMapReadyCallback, PermissionsListener, MapboxMap.OnMapClickListener,
        LocationListener, View.OnClickListener {


    private static final String TAG = "DemoActivity";

    Button btnNavigation;
    RecyclerView recyclerViewMapCategory;

    ImageView ivSlidingLayoutIndicator;
    Button btnMapLayerData;
    Button btnMapLayerSwitch;
    Button btnGoThere;

    private SlidingUpPanelLayout mLayout;
    private PermissionsManager permissionsManager;
    private MapView mapView;
    private MapboxMap mapboxMap;


    String filename = "";

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

    // 1. create entityList which item data extend SectionMultiEntity
    private List<SectionMultipleItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoicGVhY2VuZXBhbCIsImEiOiJjand6d3U1Zm4ycDhvNGJwNXBoazB2bHVsIn0.A-aBUuDaGi01JNFhYmzAtA");
        setContentView(R.layout.activity_map_main);

        setupToolbar("Explore", false);
        initUI(savedInstanceState);
    }

    private void initUI(Bundle savedInstanceState) {
        btnNavigation = findViewById(R.id.navigation);
        recyclerViewMapCategory = findViewById(R.id.recyclerViewMapCategory);
        ivSlidingLayoutIndicator = findViewById(R.id.iv_sliding_layout_indicator);
        btnMapLayerData = findViewById(R.id.btnMapLayerData);
        btnMapLayerSwitch = findViewById(R.id.btnMapLayerSwitch);
        btnGoThere = findViewById(R.id.btnGoThere);

        btnNavigation.setOnClickListener(this);
        btnGoThere.setOnClickListener(this);
        btnMapLayerData.setOnClickListener(this);
        btnMapLayerSwitch.setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        sharedPreferenceUtils = new SharedPreferenceUtils(MapMainActivity.this);

        setupBottomSlidingPanel();
        setupListMarkerDetailsRecycler();
        mData = DataServer.getMapDatacategoryList(MapMainActivity.this);
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
                        break;

                    case "DRAGGING":
                        ivSlidingLayoutIndicator.setBackground(getResources().getDrawable(R.drawable.ic_sliding_neutral_white_24dp));
                        break;

                    case "EXPANDED":
                        ivSlidingLayoutIndicator.setBackground(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_white_24dp));
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

    private void setupListMarkerDetailsRecycler() {
        MarkerDetailedDisplayAdapter markerDetailedDisplayAdapter = new MarkerDetailedDisplayAdapter(R.layout.marker_detailed_info_display_layout, null);
        recyclerViewMapCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMapCategory.setAdapter(markerDetailedDisplayAdapter);

    }

    private Dialog setupMapOptionsDialog() {
        // launch new intent instead of loading fragment

        int MAP_OVERLAY_ID = sharedPreferenceUtils.getIntValue(MAP_OVERLAY_LAYER, -1);

        return DialogFactory.createBaseLayerDialog(MapMainActivity.this, new DialogFactory.CustomBaseLayerDialogListner() {
            @Override
            public void onStreetClick() {
                mapView.setStyleUrl(getResources().getString(R.string.mapbox_style_mapbox_streets));
                if (MAP_OVERLAY_ID == KEY_MUNICIPAL_BOARDER) {
                    onMetropolitanClick();
                } else if (MAP_OVERLAY_ID == KEY_WARD) {
                    onWardClick();
                }

            }

            @Override
            public void onSatelliteClick() {
                mapView.setStyleUrl(getResources().getString(R.string.mapbox_style_satellite));
                if (MAP_OVERLAY_ID == KEY_MUNICIPAL_BOARDER) {
                    onMetropolitanClick();
                } else if (MAP_OVERLAY_ID == KEY_WARD) {
                    onWardClick();
                }

            }

            @Override
            public void onOpenStreetClick() {
                if (MAP_OVERLAY_ID == KEY_MUNICIPAL_BOARDER) {
                    onMetropolitanClick();
                } else if (MAP_OVERLAY_ID == KEY_WARD) {
                    onWardClick();
                }

            }

            @Override
            public void onMetropolitanClick() {
                filename = "kathmandu_boundary.geojson";
                drawGeoJsonOnMap.readAndDrawGeoSonFileOnMap(filename, true, "");
                removeLayerFromMap("kathmandu_wards.geojson");
            }

            @Override
            public void onWardClick() {
                filename = "kathmandu_wards.geojson";
                drawGeoJsonOnMap.readAndDrawGeoSonFileOnMap(filename, true, "");
                removeLayerFromMap("kathmandu_boundary.geojson");


            }

            private void removeLayerFromMap(String filename) {

                if(filename != null) {
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
        });


    }

    private Dialog setupMapDataLayerDialog(boolean isFirstTime) {

        if (mapboxMap == null) {
            Toast.makeText(this, "Your map is not ready yet", Toast.LENGTH_SHORT).show();
        }

        return DialogFactory.createMapDataLayerDialog(MapMainActivity.this, mData, drawGeoJsonOnMap, isFirstTime, new MapDataLayerDialogCloseListen() {
                    @Override
                    public void onDialogClose() {
                        drawGeoJsonOnMap();
                    }

                    @Override
                    public void isFirstTime() {
                        drawGeoJsonOnMap();

                    }
                }
        );
    }

    private void drawGeoJsonOnMap() {

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
                        Log.d(TAG, "onNext: "+mapDataLayerListCheckedEvent.getMultiItemSectionModel().getData_key());
                        Log.d(TAG, "onNext: "+mapDataLayerListCheckedEvent.getChecked());

                        drawGeoJsonOnMap.readAndDrawGeoSonFileOnMap(mapDataLayerListCheckedEvent.getMultiItemSectionModel().getData_value(),
                                mapDataLayerListCheckedEvent.getChecked(), mapDataLayerListCheckedEvent.getMultiItemSectionModel().getImage());

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
    DrawRouteOnMap drawRouteOnMap;
    MapboxBaseStyleUtils mapboxBaseStyleUtils;

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        this.mapboxMap = mapboxMap;
        setMapCameraPosition();
        mapboxMap.addOnMapClickListener(this);
        mapboxMap.getUiSettings().setCompassFadeFacingNorth(false);
        mapboxMap.getUiSettings().setCompassImage(getResources().getDrawable(R.drawable.direction_compas_icon));


        drawGeoJsonOnMap = new DrawGeoJsonOnMap(MapMainActivity.this, mapboxMap, mapView);
        drawRouteOnMap = new DrawRouteOnMap(MapMainActivity.this, mapboxMap, mapView);
        mapboxBaseStyleUtils = new MapboxBaseStyleUtils(MapMainActivity.this, mapboxMap, mapView);
        mapboxBaseStyleUtils.changeBaseColor();

        setupMapOptionsDialog();

        setupMapDataLayerDialog(true).hide();

        mapView.invalidate();
    }

    public void setMapCameraPosition() {

        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(27.657531140175244, 85.46161651611328)) // Sets the new camera position
                .zoom(11) // Sets the zoom
                .bearing(0) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 7000);

        enableLocationComponent();
    }


    @Override
    public void onMapClick(@NonNull LatLng latLngPoint) {

        if (point) {
            return;
        }

        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationCoord = latLngPoint;
//        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
//                .position(destinationCoord)
//        );


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Toast.makeText(this, "Location Permission Required", Toast.LENGTH_SHORT).show();
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
//            drawRouteOnMap.getRoute(originPosition, destinationPosition);
//            navigation.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Searching current location \nMake sure your GPS provider is enabled.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

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
            locationComponent.setCameraMode(CameraMode.TRACKING);
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
        EventBus.getDefault().register(this);
        mapView.onStart();


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
        EventBus.getDefault().unregister(this);
        mapView.onStop();
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
                .zoom(11.5) // Sets the zoom
                .bearing(0) // Rotate the camera
                .tilt(30) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        Log.d("MapBox", "animateCameraPosition: ");


        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 3000);
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
            drawRouteOnMap.getRoute(originPosition, destinationPosition);
            btnNavigation.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Searching current location \nMake sure your GPS provider is enabled.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    List<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent> mapDataLayerListCheckedEventList = new ArrayList<MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent>();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRVItemClick(MapDataLayerListCheckEvent.MapDataLayerListCheckedEvent itemClick) {
        String name = itemClick.getMultiItemSectionModel().getData_value();
//        if(itemClick.getChecked()){
        if (mapDataLayerListCheckedEventList.size() == 0) {
            mapDataLayerListCheckedEventList.add(itemClick);

//            hashMapDataLayer.put(itemClick.getMultiItemSectionModel().getData_key(), itemClick);

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
//        Toast.makeText(this, "add to your circle button clicked " + name, Toast.LENGTH_SHORT).show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMarkerItemClick(MarkerClickEvent.MarkerItemClick itemClick) {

        List<MarkerDetailsKeyValue> markerDetailsKeyValueListCommn = new ArrayList<MarkerDetailsKeyValue>();
        selectedMarkerPosition = itemClick.getLocation();
        if (selectedMarkerPosition == null) {
            btnGoThere.setVisibility(View.GONE);
        } else {
            btnGoThere.setVisibility(View.VISIBLE);
        }

        String markerPropertiesJson = itemClick.getMarkerProperties();
        QueryBuildWithSplitter queryBuildWithSplitter = new QueryBuildWithSplitter();
        markerDetailsKeyValueListCommn = queryBuildWithSplitter.splitedKeyValueList(
                queryBuildWithSplitter.splitedStringList(markerPropertiesJson));

        ((MarkerDetailedDisplayAdapter) recyclerViewMapCategory.getAdapter()).replaceData(markerDetailsKeyValueListCommn);

        mLayout.setAnchorPoint(0.52f);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

        drawRouteOnMap.removeRoute();

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
        Toast.makeText(this, "Please enable your GPS Provider", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.navigation:
                drawRouteOnMap.enableNavigationUiLauncher(MapMainActivity.this);

                btnNavigation.setVisibility(View.GONE);
                break;

            case R.id.btnMapLayerData:
                if (mapboxMap == null) {
                    Toast.makeText(this, "Your map is not ready yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                mapboxMap.clear();
                setupMapOptionsDialog();
                mapDataLayerListCheckedEventList.clear();
                setupMapDataLayerDialog(false).show();
                break;

            case R.id.btnMapLayerSwitch:
                setupMapOptionsDialog().show();
                break;

            case R.id.btnGoThere:
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    generateRouteToGoThere(selectedMarkerPosition);
                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

}