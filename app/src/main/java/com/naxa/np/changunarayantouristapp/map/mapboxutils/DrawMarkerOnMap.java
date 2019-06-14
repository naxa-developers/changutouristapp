package com.naxa.np.changunarayantouristapp.map.mapboxutils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.plugins.cluster.clustering.ClusterItem;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.events.MarkerClickEvent;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DrawMarkerOnMap implements MapboxMap.OnInfoWindowClickListener,
        MapboxMap.OnMarkerClickListener {
    private static final String TAG = "DrawMarkerOnMap";

    AppCompatActivity context;
    MapboxMap mapboxMap;
    MapView mapView;


    String imageName;
    ArrayList<LatLng> points = null;
    StringBuilder geoJsonString;


    public DrawMarkerOnMap(AppCompatActivity context, MapboxMap mapboxMap, MapView mapView) {
        this.context = context;
        this.mapboxMap = mapboxMap;
        this.mapView = mapView;
    }

        List<MyItem> items = new ArrayList<MyItem>();
    public void AddMarkerOnMap(String geoJsonFileName ,StringBuilder stringBuilder, String imageName){


        Log.d(TAG, "AddMarkerOnMap: "+stringBuilder.toString());
        Icon icon ;

        if((LoadImageUtils.getImageBitmapFromDrawable(context, imageName)) == null){
            icon = IconFactory.getInstance(context).fromResource(R.drawable.mapbox_marker_icon_default);
        }else {
            icon = IconFactory.getInstance(context).fromBitmap(LoadImageUtils.getImageBitmapFromDrawable(context, imageName));
        }

      if(stringBuilder == null){
          return;
      }

        String geoJson = stringBuilder.toString();


        FeatureCollection featureCollection = FeatureCollection.fromJson(geoJson);
        List<Feature> featureList  = featureCollection.features();

        Observable.just(featureList)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(new Function<List<Feature>, Iterable<Feature>>() {
                    @Override
                    public Iterable<Feature> apply(List<Feature> features) throws Exception {
                        return features;
                    }
                })
                .map(new Function<Feature, Feature>() {
                    @Override
                    public Feature apply(Feature feature) throws Exception {
                        return feature;
                    }
                })
                .subscribe(new DisposableObserver<Feature>() {
                    @Override
                    public void onNext(Feature feature) {


//                        String title = feature.getStringProperty("name");

                        String snippest = feature.toString();
                        Log.d(TAG, "onNext: JSON Object "+snippest);
                        Log.d(TAG, "onNext: JSON Object Geometry "+feature.geometry().toJson());


                        LatLng location = new LatLng(0.0, 0.0);
                        try {
                            JSONObject jsonObject = new JSONObject(feature.geometry().toJson());
                            Log.d(TAG, "onNext: JSON Object Co-ordinates "+jsonObject.getJSONArray("coordinates").getString(0));
                            String Lon = jsonObject.getJSONArray("coordinates").getString(0) ;
                            String Lat = jsonObject.getJSONArray("coordinates").getString(1) ;
                            location = new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lon));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                         Add the custom icon marker to the map
                        Marker marker = mapboxMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location))
//                                .title(title)
                                .title("title")
                                .snippet(snippest)
                                .icon(icon));
////
//                        items.add(new MyItem(location,title,snippest, icon));
                        items.add(new MyItem(location,"title",snippest, icon));

                        mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                Toast.makeText(context, "Marker tapped: " + marker.getTitle(), Toast.LENGTH_LONG).show();
                                onInfoWindowClick(marker);
                                animateCameraPosition(marker.getPosition());
                                return true;
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: "+items.size());

//                        runThread(items);
                    }
                });

    }



    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        String snippest = marker.getSnippet();
        Log.d(TAG, "onMarkerClick: "+snippest);

        return false;
    }

    @Override
    public boolean onInfoWindowClick(@NonNull Marker marker) {
        String snippest = marker.getSnippet();
        EventBus.getDefault().post(new MarkerClickEvent.MarkerItemClick(snippest, marker.getPosition()));

        Log.d(TAG, "onMarkerClick: "+snippest);
        return false;
    }


    public void animateCameraPosition(LatLng location) {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(location)) // Sets the new camera position
//                .zoom(14) // Sets the zoom
                .bearing(0) // Rotate the camera
                .tilt(10) // Set the camera tilt
                .build(); // Creates a CameraPosition from the builder

        Log.d("MapBox", "animateCameraPosition: ");


        mapboxMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), 1000);
    }


    /**
     * Custom class for use by the marker cluster plugin
     */
    public static class MyItem implements ClusterItem {
        private final LatLng position;
        private String title;
        private String snippet;
        private Icon icon;

        public MyItem(double lat, double lng) {
            position = new LatLng(lat, lng);
            title = null;
            snippet = null;
        }


        public MyItem(double lat, double lng, String title, String snippet, Icon icon) {
            position = new LatLng(lat, lng);
            this.title = title;
            this.snippet = snippet;
            this.icon = icon;
        }

        public MyItem(LatLng latLng, String title, String snippet, Icon icon) {
            position = latLng;
            this.title = title;
            this.snippet = snippet;
            this.icon = icon;
        }

        public MyItem(LatLng latLng, String title, String snippet) {
            position = latLng;
            this.title = title;
            this.snippet = snippet;
        }

        @Override
        public LatLng getPosition() {
            return position;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getSnippet() {
            return snippet;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSnippet(String snippet) {
            this.snippet = snippet;
        }

        public Icon getIcon() {
            return icon;
        }

        public void setIcon(Icon icon) {
            this.icon = icon;
        }
    }
}
