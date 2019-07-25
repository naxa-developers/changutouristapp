package com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DataServer {

    public static List<SectionMultipleItem> getMapDatacategoryList(@NonNull Context context){
        List<SectionMultipleItem> list = new ArrayList<>();



        list.add(new SectionMultipleItem(true, "Disaster prevention facility", false, false, "5"));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_government", "Government Buildings", "government_buildings.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_openspace", "Open Spaces", "openspace_polygons.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_openspace", "Parks", "park.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_water", "Water Supply location", "water_tower_points.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_health", "Medical institutions", "health_facilities.geojson")));

        list.add(new SectionMultipleItem(true, "Support Station", false, false, "5"));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_education", "Schools run by the Kathmandu Metropolitan Government", "educational_Institution_geojson.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_development", "Youth Clubs", "youthclub.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_hotel", "Hotel, Restaurants Area", "hotels.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_industry", "Industries", "industries.geojson")));
        list.add(new SectionMultipleItem(SectionMultipleItem.MAP_DATA_LIST, new MultiItemSectionModel("marker_transportaion", "Gas stations", "petrol_pump_points.geojson")));

        return list;
    }
}
