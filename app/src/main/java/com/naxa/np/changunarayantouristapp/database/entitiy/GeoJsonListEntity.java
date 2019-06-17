package com.naxa.np.changunarayantouristapp.database.entitiy;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "GeoJsonListEntity",
        indices = {@Index(value = "category_table",
                unique = true)})
public class GeoJsonListEntity{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("category_table")
    @ColumnInfo(name = "category_table")
    @Expose
    private String categoryName;

    @SerializedName("category_json")
    @ColumnInfo(name = "category_json")
    @Expose
    private String categoryJson;

    public GeoJsonListEntity(String categoryName, String categoryJson) {
        this.categoryName = categoryName;
        this.categoryJson = categoryJson;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryJson() {
        return categoryJson;
    }

    public void setCategoryJson(String categoryJson) {
        this.categoryJson = categoryJson;
    }

}
