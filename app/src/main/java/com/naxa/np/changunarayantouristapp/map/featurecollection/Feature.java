
package com.naxa.np.changunarayantouristapp.map.featurecollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

public class Feature {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("properties")
    @Expose
    private PlacesDetailsEntity properties;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PlacesDetailsEntity getProperties() {
        return properties;
    }

    public void setProperties(PlacesDetailsEntity properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

}
