package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.naxa.np.changunarayantouristapp.database.entitiy.PlacesDetailsEntity;

import java.util.List;

public class MainPlaceListDetailsResponse  {

    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<PlacesDetailsEntity> data = null;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PlacesDetailsEntity> getData() {
        return data;
    }

    public void setData(List<PlacesDetailsEntity> data) {
        this.data = data;
    }

}
