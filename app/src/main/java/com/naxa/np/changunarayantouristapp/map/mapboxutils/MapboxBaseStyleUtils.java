package com.naxa.np.changunarayantouristapp.map.mapboxutils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.utils.SharedPreferenceUtils;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.naxa.np.changunarayantouristapp.utils.Constant.MapKey.MAP_BOARDER_OVERLAY_LAYER_ID;

public class MapboxBaseStyleUtils {

    private MapView mapView;
    private MapboxMap map;
    Context context;
    FillLayer water;
    FillLayer building;
    FillLayer boarderLayer;



    public MapboxBaseStyleUtils(Context context , MapboxMap map, MapView mapView) {
        this.mapView = mapView;
        this.map = map;
        this.context = context;

        water = (FillLayer) map.getLayer("water");
        building = (FillLayer) map.getLayer("building");

        String layerId = SharedPreferenceUtils.getInstance(context).getStringValue(MAP_BOARDER_OVERLAY_LAYER_ID, null);
        if (!TextUtils.isEmpty(layerId)) {

            Log.d("MapboxBaseStyleUtils", "MapboxBaseStyleUtils: "+layerId);

//            boarderLayer = (FillLayer) map.getLayer(layerId);
        }

//        changeBaseColor();
    }

    public void changeBaseColor (){

        if(boarderLayer != null){
            boarderLayer.setProperties(
                    fillColor(context.getResources().getColor(R.color.green_boarder)));
            Log.d("MapboxBaseStyleUtils", "changeBaseColor: "+boarderLayer);
        }

//        Color.rgb(211,211,211))
        mapView.invalidate();
//        if(water != null){
//            water.
//        }
    }
}
