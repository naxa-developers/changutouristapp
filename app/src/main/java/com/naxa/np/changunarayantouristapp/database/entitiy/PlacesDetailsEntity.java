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

@Entity(tableName = "PlacesDetailsEntity",
        indices = {@Index(value = "qr_code",
                unique = true)})
public class PlacesDetailsEntity implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int pid;

    @SerializedName("Place Type")
    @ColumnInfo(name = "place_type")
    @Expose
    private String placeType;

    @SerializedName("Category Type")
    @ColumnInfo(name = "category_type")
    @Expose
    private String categoryType;

    @SerializedName("FID")
    @ColumnInfo(name = "fid")
    @Expose
    private Integer fID;

    @SerializedName("Name")
    @ColumnInfo(name = "name")
    @Expose
    private String name;

    @SerializedName("Type")
    @ColumnInfo(name = "type")
    @Expose
    private String type;

    @SerializedName("Description")
    @ColumnInfo(name = "description")
    @Expose
    private String description;

    @SerializedName("Photo")
    @ColumnInfo(name = "photo")
    @Expose
    private String photo ;

    @SerializedName("Primary image")
    @ColumnInfo(name = "primary_image")
    @Expose
    private String primaryImage;

    @SerializedName("Images")
    @ColumnInfo(name = "images")
    @Expose
    private String images ;

    @SerializedName("Videos")
    @ColumnInfo(name = "videos")
    @Expose
    private String videos ;

    @SerializedName("360 images")
    @ColumnInfo(name = "360_images")
    @Expose
    private String _360Images;

    @SerializedName("Audio")
    @ColumnInfo(name = "audio")
    @Expose
    private String audio ;

    @SerializedName("QR code")
    @ColumnInfo(name = "qr_code")
    @Expose
    private String qRCode;

    @SerializedName("Language")
    @ColumnInfo(name = "language")
    @Expose
    private String language;

    @SerializedName("Latitude")
    @ColumnInfo(name = "latitude")
    @Expose
    private String latitude;

    @SerializedName("Longitude")
    @ColumnInfo(name = "longitude")
    @Expose
    private String longitude;

    @SerializedName("the geom")
    @ColumnInfo(name = "the_geom")
    @Expose
    private String theGeom;

    public PlacesDetailsEntity(String placeType, String categoryType, Integer fID, String name, String type, String description, String photo, String primaryImage, String images, String videos, String _360Images, String audio, String qRCode, String language, String latitude, String longitude, String theGeom) {
        this.placeType = placeType;
        this.categoryType = categoryType;
        this.fID = fID;
        this.name = name;
        this.type = type;
        this.description = description;
        this.photo = photo;
        this.primaryImage = primaryImage;
        this.images = images;
        this.videos = videos;
        this._360Images = _360Images;
        this.audio = audio;
        this.qRCode = qRCode;
        this.language = language;
        this.latitude = latitude;
        this.longitude = longitude;
        this.theGeom = theGeom;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getfID() {
        return fID;
    }

    public void setfID(Integer fID) {
        this.fID = fID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(String primaryImage) {
        this.primaryImage = primaryImage;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getVideos() {
        return videos;
    }

    public void setVideos(String videos) {
        this.videos = videos;
    }

    public String get_360Images() {
        return _360Images;
    }

    public void set_360Images(String _360Images) {
        this._360Images = _360Images;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getqRCode() {
        return qRCode;
    }

    public void setqRCode(String qRCode) {
        this.qRCode = qRCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTheGeom() {
        return theGeom;
    }

    public void setTheGeom(String theGeom) {
        this.theGeom = theGeom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pid);
        dest.writeString(this.placeType);
        dest.writeString(this.categoryType);
        dest.writeValue(this.fID);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.description);
        dest.writeString(this.photo);
        dest.writeString(this.primaryImage);
        dest.writeString(this.images);
        dest.writeString(this.videos);
        dest.writeString(this._360Images);
        dest.writeString(this.audio);
        dest.writeString(this.qRCode);
        dest.writeString(this.language);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.theGeom);
    }

    protected PlacesDetailsEntity(@NotNull Parcel in) {
        this.pid = in.readInt();
        this.placeType = in.readString();
        this.categoryType = in.readString();
        this.fID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.type = in.readString();
        this.description = in.readString();
        this.photo = in.readString();
        this.primaryImage = in.readString();
        this.images = in.readString();
        this.videos = in.readString();
        this._360Images = in.readString();
        this.audio = in.readString();
        this.qRCode = in.readString();
        this.language = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.theGeom = in.readString();
    }

    public static final Parcelable.Creator<PlacesDetailsEntity> CREATOR = new Parcelable.Creator<PlacesDetailsEntity>() {
        @Override
        public PlacesDetailsEntity createFromParcel(Parcel source) {
            return new PlacesDetailsEntity(source);
        }

        @Override
        public PlacesDetailsEntity[] newArray(int size) {
            return new PlacesDetailsEntity[size];
        }
    };

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }
}