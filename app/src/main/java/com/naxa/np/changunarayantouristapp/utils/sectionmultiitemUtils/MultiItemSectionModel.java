package com.naxa.np.changunarayantouristapp.utils.sectionmultiitemUtils;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class MultiItemSectionModel implements Parcelable {

    String image;
    String data_key;
    String data_value;
    Drawable image_drawable;

    public MultiItemSectionModel(String image, String data_key, String data_value) {
        this.image = image;
        this.data_key = data_key;
        this.data_value = data_value;
    }

    public MultiItemSectionModel(String data_key, String data_value, Drawable image_drawable) {
        this.data_key = data_key;
        this.data_value = data_value;
        this.image_drawable = image_drawable;
    }


    protected MultiItemSectionModel(Parcel in) {
        image = in.readString();
        data_key = in.readString();
        data_value = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(data_key);
        dest.writeString(data_value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MultiItemSectionModel> CREATOR = new Creator<MultiItemSectionModel>() {
        @Override
        public MultiItemSectionModel createFromParcel(Parcel in) {
            return new MultiItemSectionModel(in);
        }

        @Override
        public MultiItemSectionModel[] newArray(int size) {
            return new MultiItemSectionModel[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getData_key() {
        return data_key;
    }

    public void setData_key(String data_key) {
        this.data_key = data_key;
    }

    public String getData_value() {
        return data_value;
    }

    public void setData_value(String data_value) {
        this.data_value = data_value;
    }


    public Drawable getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(Drawable image_drawable) {
        this.image_drawable = image_drawable;
    }
}
