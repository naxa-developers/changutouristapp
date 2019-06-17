package com.naxa.np.changunarayantouristapp.events;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class MarkerClickEvent {

    public static class MarkerItemClick{
        private String markerProperties;
        private LatLng location;

        public MarkerItemClick(String markerProperties, LatLng location) {
            this.markerProperties = markerProperties;
            this.location = location;
        }

        public String getMarkerProperties() {
            return markerProperties;
        }

        public LatLng getLocation() {
            return location;
        }
    }
}
