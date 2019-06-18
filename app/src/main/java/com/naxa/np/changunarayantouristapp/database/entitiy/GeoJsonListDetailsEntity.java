
package com.naxa.np.changunarayantouristapp.database.entitiy;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "GeoJsonListDetailsEntity",
        indices = {@Index(value = "category_table",
                unique = true)})
public class GeoJsonListDetailsEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("category_name")
    @ColumnInfo(name = "category_name")
    @Expose
    private String categoryName;

    @SerializedName("category_table")
    @ColumnInfo(name = "category_table")
    @Expose
    private String categoryTable;

    @SerializedName("category_type")
    @ColumnInfo(name = "category_type")
    @Expose
    private String categoryType;

    @SerializedName("category_photo")
    @ColumnInfo(name = "category_photo")
    @Expose
    private String categoryPhoto;

    @SerializedName("summary_list")
    @ColumnInfo(name = "summary_list")
    @Expose
    private String summaryList;

    @SerializedName("summary_name")
    @ColumnInfo(name = "summary_name")
    @Expose
    private String summaryName;

    @SerializedName("last_updated")
    @ColumnInfo(name = "last_updated")
    @Expose
    private String lastUpdated;

    @SerializedName("public_view")
    @ColumnInfo(name = "public_view")
    @Expose
    private String publicView;


    public GeoJsonListDetailsEntity(String categoryName, String categoryTable, String categoryType, String categoryPhoto, String summaryList, String summaryName, String lastUpdated, String publicView) {
        this.categoryName = categoryName;
        this.categoryTable = categoryTable;
        this.categoryType = categoryType;
        this.categoryPhoto = categoryPhoto;
        this.summaryList = summaryList;
        this.summaryName = summaryName;
        this.lastUpdated = lastUpdated;
        this.publicView = publicView;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryTable() {
        return categoryTable;
    }

    public void setCategoryTable(String categoryTable) {
        this.categoryTable = categoryTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryPhoto() {
        return categoryPhoto;
    }

    public void setCategoryPhoto(String categoryPhoto) {
        this.categoryPhoto = categoryPhoto;
    }


    public String getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(String summaryList) {
        this.summaryList = summaryList;
    }

    public String getSummaryName() {
        return summaryName;
    }

    public void setSummaryName(String summaryName) {
        this.summaryName = summaryName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getPublicView() {
        return publicView;
    }

    public void setPublicView(String publicView) {
        this.publicView = publicView;
    }
}
