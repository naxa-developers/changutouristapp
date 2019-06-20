package com.naxa.np.changunarayantouristapp.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoJsonFeatureProperties {
    @SerializedName("FID")
    @Expose
    private Object fID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("Primary image")
    @Expose
    private String primaryImage;
    @SerializedName("Images")
    @Expose
    private String images;
    @SerializedName("Videos")
    @Expose
    private String videos;
    @SerializedName("360 images")
    @Expose
    private String _360Images;
    @SerializedName("Audio")
    @Expose
    private String audio;
    @SerializedName("QR code")
    @Expose
    private String qRCode;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("the geom")
    @Expose
    private String theGeom;

    public Object getFID() {
        return fID;
    }

    public void setFID(Object fID) {
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

    public String get360Images() {
        return _360Images;
    }

    public void set360Images(String _360Images) {
        this._360Images = _360Images;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getQRCode() {
        return qRCode;
    }

    public void setQRCode(String qRCode) {
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

}