
package com.naxa.np.changunarayantouristapp.touristinformationguide;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TouristInformationGuideListResponse {

    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<TouristInformationGuideDetails> data = null;

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

    public List<TouristInformationGuideDetails> getData() {
        return data;
    }

    public void setData(List<TouristInformationGuideDetails> data) {
        this.data = data;
    }

}
