package com.naxa.np.changunarayantouristapp.placedetailsview.ratingplace;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

public class PlaceRatingResponse implements Parcelable {

    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("average")
    @Expose
    private String average;

    public PlaceRatingResponse() {
    }

    public PlaceRatingResponse(Integer error, String message, String average) {
        this.error = error;
        this.message = message;
        this.average = average;
    }

    protected PlaceRatingResponse(@NotNull Parcel in) {
        if (in.readByte() == 0) {
            error = null;
        } else {
            error = in.readInt();
        }
        message = in.readString();
        average = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (error == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(error);
        }
        dest.writeString(message);
        dest.writeString(average);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlaceRatingResponse> CREATOR = new Creator<PlaceRatingResponse>() {
        @Override
        public PlaceRatingResponse createFromParcel(Parcel in) {
            return new PlaceRatingResponse(in);
        }

        @Override
        public PlaceRatingResponse[] newArray(int size) {
            return new PlaceRatingResponse[size];
        }
    };

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

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

}