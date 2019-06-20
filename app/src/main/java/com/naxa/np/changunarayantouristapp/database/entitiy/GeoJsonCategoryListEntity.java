
package com.naxa.np.changunarayantouristapp.database.entitiy;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "GeoJsonCategoryListEntity",
        indices = {@Index(value = "category_table",
                unique = true)})
public class GeoJsonCategoryListEntity implements Parcelable {

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

    @SerializedName("category_marker")
    @ColumnInfo(name = "category_marker")
    @Expose
    private String categoryMarker;

    @SerializedName("summary_list")
    @ColumnInfo(name = "summary_list")
    @Expose
    private String summaryList;

    @SerializedName("summary_name")
    @ColumnInfo(name = "summary_name")
    @Expose
    private String summaryName;

    public GeoJsonCategoryListEntity(String categoryName, String categoryTable, String categoryMarker, String summaryList, String summaryName) {
        this.categoryName = categoryName;
        this.categoryTable = categoryTable;
        this.categoryMarker = categoryMarker;
        this.summaryList = summaryList;
        this.summaryName = summaryName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCategoryMarker() {
        return categoryMarker;
    }

    public void setCategoryMarker(String categoryMarker) {
        this.categoryMarker = categoryMarker;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.categoryName);
        dest.writeString(this.categoryTable);
        dest.writeString(this.categoryMarker);
        dest.writeString(this.summaryList);
        dest.writeString(this.summaryName);
    }

    protected GeoJsonCategoryListEntity(@NotNull Parcel in) {
        this.id = in.readInt();
        this.categoryName = in.readString();
        this.categoryTable = in.readString();
        this.categoryMarker = in.readString();
        this.summaryList = in.readString();
        this.summaryName = in.readString();
    }

    public static final Parcelable.Creator<GeoJsonCategoryListEntity> CREATOR = new Parcelable.Creator<GeoJsonCategoryListEntity>() {
        @Override
        public GeoJsonCategoryListEntity createFromParcel(Parcel source) {
            return new GeoJsonCategoryListEntity(source);
        }

        @Override
        public GeoJsonCategoryListEntity[] newArray(int size) {
            return new GeoJsonCategoryListEntity[size];
        }
    };
}
