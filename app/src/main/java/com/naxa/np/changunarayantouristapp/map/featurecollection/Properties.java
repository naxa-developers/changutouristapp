
package com.naxa.np.changunarayantouristapp.map.featurecollection;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("FID")
    @Expose
    private Integer fID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Type")
    @Expose
    private String type;
    @SerializedName("Descrption")
    @Expose
    private String descrption;
    @SerializedName("Primary image")
    @Expose
    private Object primaryImage;
    @SerializedName("Images")
    @Expose
    private Object images;
    @SerializedName("Videos")
    @Expose
    private Object videos;
    @SerializedName("360 Image")
    @Expose
    private Object _360Image;
    @SerializedName("Audio")
    @Expose
    private Object audio;
    @SerializedName("Language")
    @Expose
    private String language;
    @SerializedName("QR Code")
    @Expose
    private Object qRCode;
    @SerializedName("Latitude")
    @Expose
    private Double latitude;
    @SerializedName("Longitude")
    @Expose
    private Double longitude;
    @SerializedName("place type")
    @Expose
    private String placeType;

    public Integer getFID() {
        return fID;
    }

    public void setFID(Integer fID) {
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

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public Object getPrimaryImage() {
        return primaryImage;
    }

    public void setPrimaryImage(Object primaryImage) {
        this.primaryImage = primaryImage;
    }

    public Object getImages() {
        return images;
    }

    public void setImages(Object images) {
        this.images = images;
    }

    public Object getVideos() {
        return videos;
    }

    public void setVideos(Object videos) {
        this.videos = videos;
    }

    public Object get360Image() {
        return _360Image;
    }

    public void set360Image(Object _360Image) {
        this._360Image = _360Image;
    }

    public Object getAudio() {
        return audio;
    }

    public void setAudio(Object audio) {
        this.audio = audio;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Object getQRCode() {
        return qRCode;
    }

    public void setQRCode(Object qRCode) {
        this.qRCode = qRCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

}
