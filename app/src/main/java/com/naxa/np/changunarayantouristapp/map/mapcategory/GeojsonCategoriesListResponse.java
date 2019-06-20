
package com.naxa.np.changunarayantouristapp.map.mapcategory;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.naxa.np.changunarayantouristapp.database.entitiy.GeoJsonCategoryListEntity;

public class GeojsonCategoriesListResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<GeoJsonCategoryListEntity> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<GeoJsonCategoryListEntity> getData() {
        return data;
    }

    public void setData(List<GeoJsonCategoryListEntity> data) {
        this.data = data;
    }

}
