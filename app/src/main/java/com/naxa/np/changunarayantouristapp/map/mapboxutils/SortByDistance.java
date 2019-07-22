package com.naxa.np.changunarayantouristapp.map.mapboxutils;

import android.location.Location;

import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SortByDistance {

    double myLat = 0.0;
    double myLong = 0.0;

//    private List<PlacesDetailsEntity> sortedNearbyPlacesList;
//    private List<Float> sortedNearbyPlacesDistanceList;

    Map<PlacesDetailsEntity, Float> hashMapWithDistance;

    public SortByDistance(double myLat, double myLong) {
        this.myLat = myLat;
        this.myLong = myLong;
    }


    public synchronized LinkedHashMap sortNearbyPlaces(List<PlacesDetailsEntity> placesDetailsEntityList) {

        hashMapWithDistance = new HashMap<PlacesDetailsEntity, Float>();
                if (placesDetailsEntityList.size() > 1) {
                    for (int i = 0; i < placesDetailsEntityList.size(); i++) {
                        double latfirst = Double.parseDouble(placesDetailsEntityList.get(i).getLatitude());
                        double longfirst = Double.parseDouble(placesDetailsEntityList.get(i).getLongitude());

                        float[] result1 = new float[3];
                        Location.distanceBetween(myLat, myLong, latfirst, longfirst, result1);
                        Float distance1 = result1[0];

                        hashMapWithDistance.put(placesDetailsEntityList.get(i), distance1);
                    }
                }
        return sortMapByValuesWithDuplicates(hashMapWithDistance);

    }

    private synchronized LinkedHashMap sortMapByValuesWithDuplicates(@NotNull Map passedMap) {
        List mapKeys = new ArrayList(passedMap.keySet());
        List mapValues = new ArrayList(passedMap.values());
        Collections.sort(mapValues);
//        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                Object comp1 = passedMap.get(key);
                Float comp2 = Float.parseFloat(val.toString());

                if (comp1.equals(comp2)) {
                    passedMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((PlacesDetailsEntity) key, (Float) val);
                    break;
                }
            }
        }

//        //Getting Set of keys from HashMap
//        Set<PlacesDetailsEntity> keySet = sortedMap.keySet();
//        //Creating an ArrayList of keys by passing the keySet
//        sortedNearbyPlacesList = new ArrayList<>(keySet);
//
//
//        //Getting Collection of values from HashMap
//        Collection<Float> values = sortedMap.values();
//        //Creating an ArrayList of values
//        sortedNearbyPlacesDistanceList = new ArrayList<>(values);

        return sortedMap;
    }

}
