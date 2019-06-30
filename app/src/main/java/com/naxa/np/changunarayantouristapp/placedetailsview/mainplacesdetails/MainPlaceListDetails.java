package com.naxa.np.changunarayantouristapp.placedetailsview.mainplacesdetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainPlaceListDetails implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("qrcode")
    @Expose
    private String qrcode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;

    public MainPlaceListDetails(String id, String typeId, String locationId, String shortDesc, String description, String createdAt, String image, String language, String audio, String video, String sortOrder, String rating, String longitude, String latitude, String qrcode, String title, String slug) {
        this.id = id;
        this.typeId = typeId;
        this.locationId = locationId;
        this.shortDesc = shortDesc;
        this.description = description;
        this.createdAt = createdAt;
        this.image = image;
        this.language = language;
        this.audio = audio;
        this.video = video;
        this.sortOrder = sortOrder;
        this.rating = rating;
        this.longitude = longitude;
        this.latitude = latitude;
        this.qrcode = qrcode;
        this.title = title;
        this.slug = slug;
    }

    public MainPlaceListDetails() {
    }

    protected MainPlaceListDetails(Parcel in) {
        id = in.readString();
        typeId = in.readString();
        locationId = in.readString();
        shortDesc = in.readString();
        description = in.readString();
        createdAt = in.readString();
        image = in.readString();
        language = in.readString();
        audio = in.readString();
        video = in.readString();
        sortOrder = in.readString();
        rating = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        qrcode = in.readString();
        title = in.readString();
        slug = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(typeId);
        dest.writeString(locationId);
        dest.writeString(shortDesc);
        dest.writeString(description);
        dest.writeString(createdAt);
        dest.writeString(image);
        dest.writeString(language);
        dest.writeString(audio);
        dest.writeString(video);
        dest.writeString(sortOrder);
        dest.writeString(rating);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(qrcode);
        dest.writeString(title);
        dest.writeString(slug);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainPlaceListDetails> CREATOR = new Creator<MainPlaceListDetails>() {
        @Override
        public MainPlaceListDetails createFromParcel(Parcel in) {
            return new MainPlaceListDetails(in);
        }

        @Override
        public MainPlaceListDetails[] newArray(int size) {
            return new MainPlaceListDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}