package com.naxa.np.changunarayantouristapp.map.mapboxutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.daasuu.bl.BubbleLayout;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.utils.imageutils.LoadImageUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.mapbox.mapboxsdk.style.expressions.Expression.all;
import static com.mapbox.mapboxsdk.style.expressions.Expression.division;
import static com.mapbox.mapboxsdk.style.expressions.Expression.eq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.exponential;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.gte;
import static com.mapbox.mapboxsdk.style.expressions.Expression.has;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lt;
import static com.mapbox.mapboxsdk.style.expressions.Expression.neq;
import static com.mapbox.mapboxsdk.style.expressions.Expression.rgb;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.expressions.Expression.toNumber;
import static com.mapbox.mapboxsdk.style.layers.Property.ICON_ANCHOR_BOTTOM;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAnchor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class DrawGeoJsonOnMap implements MapboxMap.OnMapClickListener, MapboxMap.OnInfoWindowClickListener {

    String TAG = "DrawGeoJsonOnMap";
    String filename = "";
    AppCompatActivity context;
    MapboxMap mapboxMap;
    MapView mapView;


    ArrayList<LatLng> points = null;
    StringBuilder geoJsonString;


//    String imageName;


    String markerImageId = "MARKER_IMAGE_ID_";
    String markerLayerId = "MARKER_LAYER_ID_";
    String markerCalloutLayerId = "CALLOUT_LAYER_ID_";

    private String MARKER_IMAGE_ID = "MARKER_IMAGE_ID";
    private String MARKER_LAYER_ID = "MARKER_LAYER_ID";
    private String CALLOUT_LAYER_ID = "CALLOUT_LAYER_ID";


    private String PROPERTY_SELECTED = "selected";
    private String PROPERTY_NAME = "name";
    private String PROPERTY_CAPITAL = "Address";
    private String geojsonSourceId = "geojsonSourceId";
    private String geojsonLayerId = "geojsonLayerId";
    private GeoJsonSource source;
    private FeatureCollection featureCollection;
    private HashMap<String, View> viewMap;

    List<String> filenameList;

//    boolean isChecked;

    public DrawGeoJsonOnMap(AppCompatActivity context, MapboxMap mapboxMap, MapView mapView) {
        this.context = context;
        this.mapboxMap = mapboxMap;
        this.mapView = mapView;

        filenameList = new ArrayList<String>();
    }


    public void readAndDrawGeoSonFileOnMap(String geoJsonFileName, Boolean isChecked, String imageName) {

//        this.imageName = imageName;
//        this.isChecked = isChecked;
        filename = geoJsonFileName;

        try {
            geojsonSourceId = geoJsonFileName;
            geojsonLayerId = geoJsonFileName;
            MARKER_IMAGE_ID = markerImageId + geoJsonFileName;
            MARKER_LAYER_ID = markerLayerId + geoJsonFileName;
            CALLOUT_LAYER_ID = markerCalloutLayerId + geoJsonFileName;


//            if(mapboxMap.getSource(geojsonSourceId)!= null) {
//                mapboxMap.removeLayer(geojsonLayerId);
//                mapboxMap.removeLayer(MARKER_LAYER_ID);
//            mapboxMap.removeLayer(CALLOUT_LAYER_ID);

//            }
//            if(mapboxMap.getLayer(geojsonLayerId) != null){

//                mapboxMap.removeSource(geojsonSourceId);
//            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        io.reactivex.Observable.just(geoJsonFileName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String filename) {

                        // Load GeoJSON file
                        try {
                            InputStream inputStream = context.getAssets().open(filename);
                            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                            StringBuilder sb = new StringBuilder();
                            int cp;
                            while ((cp = rd.read()) != -1) {
                                sb.append((char) cp);
                            }
                            geoJsonString = sb;

                            inputStream.close();


                            drawGeoJsonOnMap(geoJsonString, isChecked, imageName);


                        } catch (IOException e) {
                            e.printStackTrace();
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

    private void drawGeoJsonOnMap(@NotNull StringBuilder geoJsonString, Boolean isChecked, String imageName) {
        GeoJsonSource source = new GeoJsonSource(geojsonSourceId, geoJsonString.toString());
        String type = "";
        try {

            JSONObject json = new JSONObject(geoJsonString.toString());
            JSONArray features = json.getJSONArray("features");
            JSONObject geometry = (features.getJSONObject(0)).getJSONObject("geometry");
            if (geometry != null) {
                type = geometry.getString("type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (type.equals("Point") || type.equals("point") || type.equals("POINT")) {

            DrawMarkerOnMap drawMarkerOnMap = new DrawMarkerOnMap(context, mapboxMap, mapView);

            if (isChecked) {
//                    plotMarkerOnMap(geoJsonString, context, imageName, isChecked);

                if (geoJsonString != null) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            drawMarkerOnMap.AddMarkerOnMap(filename, geoJsonString, imageName);

                        }
                    }, 50);
                }


            } else {
                if (geoJsonString != null) {
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                //Do something after 100ms
//                                drawMarkerOnMap.RemoveMarkerOnMap(filename, geoJsonString, imageName);
//
//                            }
//                        }, 50);
//                        drawMarkerOnMap.RemoveMarkerOnMap(filename, geoJsonString, imageName);
                }
                mapboxMap.removeLayer(geojsonLayerId);
                mapboxMap.removeLayer(MARKER_LAYER_ID);
                mapboxMap.removeLayer(CALLOUT_LAYER_ID);
                mapboxMap.removeSource(geojsonSourceId);
                mapView.invalidate();
                return;
            }


        } else {

            if (mapboxMap.getSource(geojsonSourceId) == null) {
                mapboxMap.addSource(source);
            }

            LineLayer lineLayer = new LineLayer(geojsonLayerId, geojsonSourceId);
            lineLayer.setProperties(
                    PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                    PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                    PropertyFactory.lineWidth(2f),
                    PropertyFactory.lineColor(context.getResources().getColor(R.color.colorPrimary))
            );

            if (isChecked) {

                if (mapboxMap.getLayer(geojsonLayerId) == null) {


                    mapboxMap.addLayer(lineLayer);
                    JSONObject geoJsonObj = null;
                    try {
                        geoJsonObj = new JSONObject(geoJsonString.toString());

                        JSONObject jsonObject = geoJsonObj.optJSONArray("features").optJSONObject(0).optJSONObject("properties");
                        if (geoJsonObj.optJSONArray("features").optJSONObject(0).has("bbox")) {
                            JSONArray boundingBox = geoJsonObj.optJSONArray("features").optJSONObject(0).optJSONArray("bbox");


                            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                                    .include(new LatLng(Double.parseDouble(boundingBox.optString(1)), Double.parseDouble(boundingBox.optString(0)))) // Northeast
                                    .include(new LatLng(Double.parseDouble(boundingBox.optString(4)), Double.parseDouble(boundingBox.optString(3)))) // Southwest
                                    .build();
                            mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
                            mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 60), 2000);
                            mapView.invalidate();


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            } else {
                mapboxMap.removeLayer(lineLayer);
            }
            mapView.invalidate();
        }


    }

    private WeakReference<AppCompatActivity> activityRef;
    StringBuilder stringBuilderGeoJson;

    //    int featureCollectionCount = 0;
    private void plotMarkerOnMap(StringBuilder geoJsonString, AppCompatActivity activity, String imageName, boolean isChecked) {
        this.activityRef = new WeakReference<>(activity);

//        featureCollectionCount++;

        final FeatureCollection[] featureCollection = {null};
        mapboxMap.addOnMapClickListener(this);

        if (activity == null) {
            return;
        }

        io.reactivex.Observable.just(geoJsonString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<StringBuilder>() {
                    @Override
                    public void onNext(StringBuilder stringBuilder) {

                        try {
                            stringBuilderGeoJson = stringBuilder;

                            featureCollection[0] = FeatureCollection.fromJson(stringBuilder.toString());


                            addClusteredGeoJsonSource(stringBuilder.toString());
                            mapboxMap.addImage("cross-icon-id", LoadImageUtils.getImageBitmapFromDrawable(context, imageName));
                        } catch (Exception exception) {
                            Log.e("MAPBOX", "Exception Loading GeoJSON: " + exception.toString());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {


//                        OpenSpaceMapActivity activity = activityRef.get();
//                        if (featureCollection == null || activity == null) {
//                            return;
//                        }

// This example runs on the premise that each GeoJSON Feature has a "selected" property,
// with a boolean value. If your data's Features don't have this boolean property,
// add it to the FeatureCollection 's features with the following code:
//                        for (Feature singleFeature : featureCollection[0].features()) {
//                            singleFeature.addBooleanProperty(PROPERTY_SELECTED, false);
//                        }
//
//                        setUpData(featureCollection[0] , imageName, isChecked );
//
//                        new GenerateViewIconTask(activity).execute(featureCollection);


                    }
                });


    }


    private void addClusteredGeoJsonSource(String geoJson) {

// Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            mapboxMap.addSource(
// Point to GeoJSON data. This example visualizes all M1.0+ geojson
                    new GeoJsonSource("geojsonSourceId",
                            new String(geoJson),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )
            );
        } catch (NullPointerException malformedUrlException) {
            Log.e("dataClusterActivity", "Check the URL " + malformedUrlException.getMessage());
        }


// Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
// Each point range gets a different fill color.
        int[][] layers = new int[][]{
                new int[]{150, ContextCompat.getColor(context, R.color.mapboxRed)},
                new int[]{20, ContextCompat.getColor(context, R.color.mapboxYellow)},
                new int[]{0, ContextCompat.getColor(context, R.color.mapbox_blue)}
        };

//Creating a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "geojsonSourceId");

        unclustered.setProperties(
                iconImage("cross-icon-id"),
                iconSize(
                        division(
                                get("mag"), literal(4.0f)
                        )
                ),
                iconColor(
                        interpolate(exponential(1), get("mag"),
                                stop(2.0, rgb(0, 255, 0)),
                                stop(4.5, rgb(0, 0, 255)),
                                stop(7.0, rgb(255, 0, 0))
                        )
                )
        );
        mapboxMap.addLayer(unclustered);

        for (int i = 0; i < layers.length; i++) {
//Add clusters' circles
            CircleLayer circles = new CircleLayer("cluster-" + i, "geojsonSourceId");
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(18f)
            );


            Expression pointCount = toNumber(get("point_count"));

// Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                    i == 0
                            ? all(has("point_count"),
                            gte(pointCount, literal(layers[i][0]))
                    ) : all(has("point_count"),
                            gt(pointCount, literal(layers[i][0])),
                            lt(pointCount, literal(layers[i - 1][0]))
                    )
            );
            mapboxMap.addLayer(circles);
        }

//Add the count labels
        SymbolLayer count = new SymbolLayer("count", "geojsonSourceId");
        count.setProperties(
                textField(Expression.toString(get("point_count"))),
                textSize(12f),
                textColor(Color.WHITE),
                textIgnorePlacement(true),
                textAllowOverlap(true)
        );
        mapboxMap.addLayer(count);

    }

    @Override
    public boolean onInfoWindowClick(@NonNull Marker marker) {
        Log.d(TAG, "onInfoWindowClick: " + marker.getTitle());
        return false;
    }


    /**
     * AsyncTask to generate Bitmap from Views to be used as iconImage in a SymbolLayer.
     * <p>
     * Call be optionally be called to update the underlying data source after execution.
     * </p>
     * <p>
     * Generating Views on background thread since we are not going to be adding them to the view hierarchy.
     * </p>
     */
    private class GenerateViewIconTask extends AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {

        private final HashMap<String, View> viewMap = new HashMap<>();
        private final WeakReference<AppCompatActivity> activityRef;
        private final boolean refreshSource;

        GenerateViewIconTask(AppCompatActivity activity, boolean refreshSource) {
            this.activityRef = new WeakReference<>(activity);
            this.refreshSource = refreshSource;
        }

        GenerateViewIconTask(AppCompatActivity activity) {
            this(activity, false);
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            context = activityRef.get();
            if (context != null) {
                HashMap<String, Bitmap> imagesMap = new HashMap<>();
                LayoutInflater inflater = LayoutInflater.from(context);

                FeatureCollection featureCollection = params[0];

                for (Feature feature : featureCollection.features()) {

                    BubbleLayout bubbleLayout = (BubbleLayout)
                            inflater.inflate(R.layout.mapbox_marker_click_popup_bubble_layout, null);

                    String name = feature.getStringProperty(PROPERTY_NAME);
                    TextView titleTextView = bubbleLayout.findViewById(R.id.map_popup_header);
                    titleTextView.setText(name);

//                    String address = feature.getStringProperty(PROPERTY_CAPITAL) == null? "":feature.getStringProperty(PROPERTY_CAPITAL);
//                    TextView descriptionTextView = bubbleLayout.findViewById(R.id.map_popup_body);
//                    descriptionTextView.setText(address);

                    int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    bubbleLayout.measure(measureSpec, measureSpec);

                    int measuredWidth = bubbleLayout.getMeasuredWidth();

                    bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

                    Bitmap bitmap = SymbolGenerator.generate(bubbleLayout);
                    imagesMap.put(name, bitmap);
                    viewMap.put(name, bubbleLayout);
                }

                return imagesMap;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            context = activityRef.get();
            if (context != null && bitmapHashMap != null) {
                setImageGenResults(viewMap, bitmapHashMap);
                if (refreshSource) {
                    refreshSource();
                }
            }

//            addClusteredGeoJsonSource(stringBuilderGeoJson.toString());
//            mapboxMap.addImage("cross-icon-id", BitmapUtils.getBitmapFromDrawable(
//                    context.getResources().getDrawable(R.drawable.ic_marker_hospital)));
            Toast.makeText(context, "Tap On marker Information", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Utility class to generate Bitmaps for Symbol.
     * <p>
     * Bitmaps can be added to the map with {@link com.mapbox.mapboxsdk.maps.MapboxMap#addImage(String, Bitmap)}
     * </p>
     */
    private static class SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        static Bitmap generate(@NonNull View view) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }

    /**
     * Invoked when the bitmaps have been generated from a view.
     */
    public void setImageGenResults(HashMap<String, View> viewMap, HashMap<String, Bitmap> imageMap) {
        if (mapboxMap != null) {
// calling addImages is faster as separate addImage calls for each bitmap.
            mapboxMap.addImages(imageMap);
        }
// need to store reference to views to be able to use them as hitboxes for click events.
        this.viewMap = viewMap;
    }

    /**
     * Updates the display of data on the map after the FeatureCollection has been modified
     */
    private void refreshSource() {
        if (source != null && featureCollection != null) {
            source.setGeoJson(featureCollection);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        handleClickIcon(mapboxMap.getProjection().toScreenLocation(point));

    }


    /**
     * Sets up all of the sources and layers needed for this example
     *
     * @param collection the FeatureCollection to set equal to the globally-declared FeatureCollection
     */
    public void setUpData(final FeatureCollection collection, String imageName, boolean isChecked) {
        if (mapboxMap == null) {
            return;
        }
        featureCollection = collection;
        setupSource();
        setUpImage(imageName);
        setUpMarkerLayer(isChecked);
        setUpInfoWindowLayer();
    }

    /**
     * Adds the GeoJSON source to the map
     */
    private void setupSource() {

        try {

            source = new GeoJsonSource(geojsonSourceId, featureCollection);
            if (mapboxMap.getSource(geojsonSourceId) == null) {
                mapboxMap.addSource(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the marker image to the map for use as a SymbolLayer icon
     */
    private void setUpImage(String imageName) {

        Bitmap icon = BitmapFactory.decodeResource(
                context.getResources(), LoadImageUtils.getImageResIDFromDrawable(context, imageName));
        if (imageName != null && !imageName.equals("")) {
            try {

                mapboxMap.addImage(MARKER_IMAGE_ID, icon);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Setup a layer with maki icons, eg. west coast city.
     */
    private void setUpMarkerLayer(boolean isChecked) {
//        mapboxMap.removeLayer(MARKER_LAYER_ID);
        if (isChecked) {
            if (mapboxMap.getLayer(MARKER_LAYER_ID) == null) {
                mapboxMap.addLayer(new SymbolLayer(MARKER_LAYER_ID, geojsonSourceId)
                        .withProperties(
                                iconImage(MARKER_IMAGE_ID),
                                iconAllowOverlap(true)
                        ));
            }
        } else {
            mapboxMap.removeLayer(new SymbolLayer(MARKER_LAYER_ID, geojsonSourceId)
                    .withProperties(
                            iconImage(MARKER_IMAGE_ID),
                            iconAllowOverlap(true)
                    ));
        }
    }

    /**
     * Setup a layer with Android SDK call-outs
     * <p>
     * name of the feature is used as key for the iconImage
     * </p>
     */
    private void setUpInfoWindowLayer() {
        mapboxMap.removeLayer(new SymbolLayer(CALLOUT_LAYER_ID, geojsonSourceId));

        mapboxMap.addLayer(new SymbolLayer(CALLOUT_LAYER_ID, geojsonSourceId)
                .withProperties(
                        /* show image with id title based on the value of the name feature property */
                        iconImage("{name}"),

                        /* set anchor of icon to bottom-left */
                        iconAnchor(ICON_ANCHOR_BOTTOM),

                        /* all info window and marker image to appear at the same time*/
                        iconAllowOverlap(true),

                        /* offset the info window to be above the marker */
                        iconOffset(new Float[]{-2f, -25f})
                )
/* add a filter to show only when selected feature property is true */
                .withFilter(eq((get(PROPERTY_SELECTED)), literal(true))));
    }

    /**
     * This method handles click events for SymbolLayer symbols.
     * <p>
     * When a SymbolLayer icon is clicked, we moved that feature to the selected state.
     * </p>
     *
     * @param screenPoint the point on screen clicked
     */
    private void handleClickIcon(PointF screenPoint) {
        List<Feature> features = mapboxMap.queryRenderedFeatures(screenPoint, MARKER_LAYER_ID);
        if (!features.isEmpty()) {
            String name = features.get(0).getStringProperty(PROPERTY_NAME);
            List<Feature> featureList = featureCollection.features();
            for (int i = 0; i < featureList.size(); i++) {
                if (featureList.get(i).getStringProperty(PROPERTY_NAME).equals(name)) {
                    if (featureSelectStatus(i)) {
                        setFeatureSelectState(featureList.get(i), false);
                    } else {
                        setSelected(i);
                    }
                }
            }
        }
    }

    /**
     * Checks whether a Feature's boolean "selected" property is true or false
     *
     * @param index the specific Feature's index position in the FeatureCollection's list of Features.
     * @return true if "selected" is true. False if the boolean property is false.
     */
    private boolean featureSelectStatus(int index) {
        if (featureCollection == null) {
            return false;
        }
        return featureCollection.features().get(index).getBooleanProperty(PROPERTY_SELECTED);
    }

    /**
     * Selects the state of a feature
     *
     * @param feature the feature to be selected.
     */
    private void setFeatureSelectState(@NotNull Feature feature, boolean selectedState) {
        feature.properties().addProperty(PROPERTY_SELECTED, selectedState);
        refreshSource();
    }

    /**
     * Set a feature selected state.
     *
     * @param index the index of selected feature
     */
    private void setSelected(int index) {
        Feature feature = featureCollection.features().get(index);
        setFeatureSelectState(feature, true);
        refreshSource();
    }
}
