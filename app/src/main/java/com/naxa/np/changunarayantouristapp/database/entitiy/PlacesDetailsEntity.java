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
        indices = {@Index(value = "qr_language",
                unique = true)})
public class PlacesDetailsEntity implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int pid;

    @SerializedName("place type")
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
    private String FID;

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

    @SerializedName("360 Image")
    @ColumnInfo(name = "360_images")
    @Expose
    private String _360Images;

    @SerializedName("Audio")
    @ColumnInfo(name = "audio")
    @Expose
    private String audio ;

    @SerializedName("QR Code")
    @ColumnInfo(name = "qr_code")
    @Expose
    private String QRCode;

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

    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    private String id;

    @SerializedName("the geom")
    @ColumnInfo(name = "the_geom")
    @Expose
    private String theGeom;

    @SerializedName("qr_language")
    @ColumnInfo(name = "qr_language")
    @Expose
    private String qrLanguage;


    public PlacesDetailsEntity(@NotNull String placeType, @NotNull String categoryType, String FID, String name, @NotNull String type, String description, String photo, String primaryImage, String images, String videos, String _360Images, String audio, String QRCode, String language, String latitude, String longitude, String id, String theGeom, String qrLanguage) {
        this.placeType = placeType.trim();
        this.categoryType = categoryType.trim();
        this.FID = FID;
        this.name = name;
        this.type = type.trim();
        this.description = description;
        this.photo = photo;
        this.primaryImage = primaryImage;
        this.images = images;
        this.videos = videos;
        this._360Images = _360Images;
        this.audio = audio;
        this.QRCode = QRCode;
        this.language = language;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.theGeom = theGeom;
        this.qrLanguage = qrLanguage;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPlaceType() {
        if(placeType == null){
            return placeType;
        }else {
            return placeType.trim();
        }
    }

    public void setPlaceType(String placeType) {
        if(placeType == null){
            this.placeType = placeType;
        }else {
            this.placeType = placeType.trim();
        }
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        if(type == null){
            return type;
        }else {
            return type.trim();
        }
    }

    public void setType(String type) {
        if(type == null){
            this.type = type;
        }else {
            this.type = type.trim();
        }    }

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

    public String getFID() {
        return FID;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setFID(String FID) {
        this.FID = FID;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
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
        dest.writeString(this.FID);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.description);
        dest.writeString(this.photo);
        dest.writeString(this.primaryImage);
        dest.writeString(this.images);
        dest.writeString(this.videos);
        dest.writeString(this._360Images);
        dest.writeString(this.audio);
        dest.writeString(this.QRCode);
        dest.writeString(this.language);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.id);
        dest.writeString(this.theGeom);
        dest.writeString(this.qrLanguage);
    }

    protected PlacesDetailsEntity(Parcel in) {
        this.pid = in.readInt();
        this.placeType = in.readString();
        this.categoryType = in.readString();
        this.FID = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.description = in.readString();
        this.photo = in.readString();
        this.primaryImage = in.readString();
        this.images = in.readString();
        this.videos = in.readString();
        this._360Images = in.readString();
        this.audio = in.readString();
        this.QRCode = in.readString();
        this.language = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.id = in.readString();
        this.theGeom = in.readString();
        this.qrLanguage = in.readString();
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

    public String getQrLanguage() {
        return qrLanguage;
    }

    public void setQrLanguage(String qrLanguage) {
        this.qrLanguage = qrLanguage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}