package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private List<MainPlaceListDetails> data = null;

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

    public List<MainPlaceListDetails> getData() {
        return data;
    }

    public void setData(List<MainPlaceListDetails> data) {
        this.data = data;
    }

}
