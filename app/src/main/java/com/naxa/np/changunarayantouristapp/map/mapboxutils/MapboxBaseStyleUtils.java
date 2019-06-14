package com.naxa.np.changunarayantouristapp.map.mapboxutils;

import android.content.Context;
import android.graphics.Color;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;

public class MapboxBaseStyleUtils {

    private MapView mapView;
    private MapboxMap map;
    Context context;
    FillLayer water;
    FillLayer building;


    public MapboxBaseStyleUtils(Context context , MapboxMap map, MapView mapView) {
        this.mapView = mapView;
        this.map = map;
        this.context = context;

        water = (FillLayer) map.getLayer("water");
        building = (FillLayer) map.getLayer("building");

//        changeBaseColor();
    }

    public void changeBaseColor (){

        if(building != null){
            building.setProperties(
                    fillColor(Color.rgb(130,94,196)));
        }
//        mapView.invalidate();
//        if(water != null){
//            water.
//        }
    }
}
