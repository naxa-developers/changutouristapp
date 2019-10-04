package com.naxa.np.changunarayantouristapp.map.mapboxutils;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.ui.v5.route.OnRouteSelectionChangeListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.naxa.np.changunarayantouristapp.R;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawRouteOnMap {
    private static final String TAG = "DrawRouteOnMap";
    Context context;
    MapboxMap mapboxMap;
    MapView mapView;
    private DirectionsRoute currentRoute;
    private NavigationMapRoute navigationMapRoute;

    public DrawRouteOnMap(Context context, MapboxMap mapboxMap, MapView mapView) {
        this.context = context;
        this.mapboxMap = mapboxMap;
        this.mapView = mapView;
    }

    public void getRoute(Point origin, Point destination, TextView textView) {
        NavigationRoute.builder(context)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .alternatives(true)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);
                        setDistanceAndTime(currentRoute, textView);


                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        if(response.body().routes().size()>0){
                            navigationMapRoute.addRoutes(response.body().routes());
                            navigationMapRoute.showAlternativeRoutes(true);
                        }else {
                            Toast.makeText(context, "Routes not found", Toast.LENGTH_SHORT).show();
                        }

                        navigationMapRoute.setOnRouteSelectionChangeListener(new OnRouteSelectionChangeListener() {
                            @Override
                            public void onNewPrimaryRouteSelected(DirectionsRoute directionsRoute) {
                                currentRoute = directionsRoute;
                                setDistanceAndTime(currentRoute, textView);
//                                Toast.makeText(context, "Distance : "+ currentRoute.distance()/1000+" Km." + "\nEstimated Time : "+currentRoute.duration()/(60*60)+" Hrs." +"\n"+currentRoute.weight(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onNewPrimaryRouteSelected: ");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }

    public void enableNavigationUiLauncher (AppCompatActivity activity){
        boolean simulateRoute = false;
        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(simulateRoute)
                .build();
        // Call this method with Context from within an Activity
        NavigationLauncher.startNavigation(activity, options);
    }

    private void setDistanceAndTime(@NotNull DirectionsRoute directionsRoute, @NotNull TextView textView){
        double directionDuration = directionsRoute.duration();
        double directionDistance = directionsRoute.distance();
        String distance;
        String time;

        if(directionDistance < 1000){
             distance = "Distance: "+ getTwoDigitsOnlyAfterDecimal(directionsRoute.distance())+" Meters.";
        }else {
             distance = "Distance: "+ getTwoDigitsOnlyAfterDecimal((directionsRoute.distance()/1000))+" Km/s.";
        }

        if(directionDuration < (60*60)){
             time = "Expected Time: "+getTwoDigitsOnlyAfterDecimal((directionsRoute.duration()/(60)))+" Minutes.";
        }else {
             time = "Expected Time: "+getTwoDigitsOnlyAfterDecimal((directionsRoute.duration()/(60*60)))+" Hr/s.";
        }

        textView.setText(String.format("%s\n%s", distance, time));

    }

    private String getTwoDigitsOnlyAfterDecimal(double rawDigits){
        String twoDigitsDecimalNo = "no data";
        if(rawDigits != 0){
            twoDigitsDecimalNo = String.format("%.2f", rawDigits);
        }
        return twoDigitsDecimalNo;
    }

    public void removeRoute (){
        if(navigationMapRoute != null) {
            navigationMapRoute.updateRouteVisibilityTo(false);
        }
    }
}
