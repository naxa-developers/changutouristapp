package com.naxa.np.changunarayantouristapp.utils;

import android.text.TextUtils;
import android.util.Log;

import com.naxa.np.changunarayantouristapp.map.markerdetailspage.MarkerDetailsKeyValue;

import java.util.ArrayList;
import java.util.List;


public class QueryBuildWithSplitter {

    private static final String TAG = "QueryBuildWithSplitter";

    public static String dynamicStringSplitterWithColumnCheckQuery(String columnName, String rawStringData) {

        String splittedStringWithOR = "";

        if (!TextUtils.isEmpty(rawStringData)) {

            if (rawStringData.contains(",")) {

                String trunk = rawStringData;
//                Log.d(TAG, "dynamicStringSplitterWithColumnCheckQuery: "+trunk);
//                String temp = trunk.replaceAll("\\s+", "");
//                Log.d(TAG, "dynamicStringSplitterWithColumnCheckQuery: temp "+temp);

                String[] splitted_string = trunk.split(",");
                for (int i = 0; i < splitted_string.length; i++) {

                    if (i == 0) {
                        splittedStringWithOR = splitted_string[i].trim();
                    } else {
//                    query build with column name
                        splittedStringWithOR = splittedStringWithOR + " OR " + columnName + " LIKE :" + splitted_string[i];
                    }
                }
            } else {
                splittedStringWithOR = rawStringData.trim();
            }
        }

        Log.d(TAG, "dynamicStringSplitterWithColumnCheckQuery: " + splittedStringWithOR);

        return splittedStringWithOR;
    }


    public static String availableFacilitiesQueryBuilder(String rawStringData) {

        String splittedStringWithOR = "";

        if (!TextUtils.isEmpty(rawStringData)) {

            if (rawStringData.contains(",")) {

                if (rawStringData.contains("Emergency_Service")) {

                    String trunk = rawStringData;
//                String temp = trunk.replaceAll("\\s+", "");
                    String[] splitted_string = trunk.split(",");

                    splittedStringWithOR = " Yes ";
                    for (int i = 0; i < splitted_string.length; i++) {

                        if (splitted_string[i].trim().contentEquals("ICU_Service")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";

                        }
                        if (splitted_string[i].trim().contentEquals("Ambulance_Service")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";
                        }
                        if (splitted_string[i].trim().contentEquals("Toilet_Facility")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";
                        }
                        if (splitted_string[i].trim().contentEquals("Fire_Extinguisher")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";
                        }


                    }

                } else {
                    String trunk = rawStringData;
//                    String temp = trunk.replaceAll("\\s+", "");
                    String[] splitted_string = trunk.split(",");

                    splittedStringWithOR = " No ";
                    for (int i = 0; i < splitted_string.length; i++) {

                        if (splitted_string[i].trim().contentEquals("ICU_Service")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";

                        }
                        if (splitted_string[i].trim().contentEquals("Ambulance_Service")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";
                        }
                        if (splitted_string[i].trim().contentEquals("Toilet_Facility")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";
                        }
                        if (splitted_string[i].trim().contentEquals("Fire_Extinguisher")) {
                            String columnName = splitted_string[i].trim().toLowerCase();
                            splittedStringWithOR = splittedStringWithOR + columnName + " LIKE :" + " Yes ";
                        }


                    }
                }

            } else {
                splittedStringWithOR = " no ";
            }
        }

        Log.d(TAG, "dynamicStringSplitterWithColumnCheckQuery: " + splittedStringWithOR);

        return splittedStringWithOR;
    }


    public static int[] dynamicStringSplitterWithRangeQueryBuild(String rawStringData) {

        String splittedStringWithRange = "";
        int lowestVal = 0;
        int highestVal = 0;
        int range[] = new int[2];

        if (!TextUtils.isEmpty(rawStringData)) {

            if (rawStringData.contains(",")) {

                String trunk = rawStringData;
                String temp = trunk.replaceAll("\\s+", "");
                String[] splitted_string = temp.split("\\,");
                for (int i = 0; i < splitted_string.length; i++) {

                    if (i == 0) {
                        String firstSplittedData = splitted_string[i].trim();
                        lowestVal = Integer.parseInt(firstSplittedData.substring(0, 2));
                        range[0] = lowestVal;
                    }
                    if (i == splitted_string.length - 1) {
                        String LastSplittedData = splitted_string[i].trim();
                        highestVal = Integer.parseInt(LastSplittedData.substring(Math.max(LastSplittedData.length() - 2, 0)));
                        range[i] = highestVal;
                    }
                }
//            range query
//            splittedStringWithRange = columnName+" BETWEEN "+lowestVal+" AND "+highestVal;
//            splittedStringWithRange = lowestVal+" AND "+highestVal;

            } else {
                String rawData = rawStringData.trim();

                lowestVal = Integer.parseInt(rawData.substring(0, 2));
                highestVal = Integer.parseInt(rawData.substring(3, 2));

                range[0] = lowestVal;
                range[1] = highestVal;

//            range query
//            splittedStringWithRange = columnName+" BETWEEN "+lowestVal+" AND "+highestVal;
//            splittedStringWithRange = lowestVal+" AND "+highestVal;
            }
        }

//    return splittedStringWithRange;
        Log.d(TAG, "dynamicStringSplitterWithRangeQueryBuild: lowest " + range[0]);
        Log.d(TAG, "dynamicStringSplitterWithRangeQueryBuild: highest " + range[1]);
        return range;
    }


    public List<String> splitedStringList(String rawStringData) {

        List<String> splittedStringList = new ArrayList<String>();

        if (!TextUtils.isEmpty(rawStringData)) {
            String temp;
            if (rawStringData.contains(",")) {
                if (rawStringData.contains("{")) {
                    String trunk = rawStringData;
                    temp = trunk.replaceAll("[{}]", "");
                } else {
                    temp = rawStringData;
                }
                String[] splitted_string = temp.split(",");
                for (int i = 0; i < splitted_string.length; i++) {
                    splittedStringList.add(splitted_string[i].trim());
                }

            }
            Log.d(TAG, "SplittedStringList " + splittedStringList.get(0) + ", " + splittedStringList.size());
        }
        return splittedStringList;
    }


    public List<MarkerDetailsKeyValue> splitedKeyValueList(List<String> rawStringList) {

        List<MarkerDetailsKeyValue> splittedStringKeyValueList = new ArrayList<MarkerDetailsKeyValue>();

        if (rawStringList != null) {
            for (int i = 0; i < rawStringList.size(); i++) {
                String rawString = rawStringList.get(i);
                String temp = rawString.replaceAll("\"", "");
//                    Log.d(TAG, "SplittedStringList: "+temp);

                String[] parts = temp.split(":");

                if (parts.length != 2) {
                    continue;
                }

                String keyPart = parts[0].trim(); // key
                String valuePart = parts[1].trim(); // value

                if(!keyPart.equals("the geom")) {
                    MarkerDetailsKeyValue markerDetailsKeyValue = new MarkerDetailsKeyValue(keyPart, valuePart);
                    splittedStringKeyValueList.add(markerDetailsKeyValue);
                }

            }

        }
        return splittedStringKeyValueList;
    }


    public static RangeValue GetRange() {
        RangeValue rangeValue = null;
        rangeValue.lowest = 0;
        rangeValue.highest = 0;
        return rangeValue;
    }

    public class RangeValue {
        int lowest;
        int highest;
    }
}
