package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 9/30/2017.
 */

public class BeanGroup implements Parcelable {

    @SerializedName("CategoryId")
    public String categoryId;

    @SerializedName("CategoryName")
    public String categoryName;

    public BeanGroup(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    @Override
    public int describeContents() {
        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryName);
    }

    public BeanGroup() {
    }

    protected BeanGroup(Parcel in) {
        this.categoryId = in.readString();
        this.categoryName = in.readString();
    }

    public static final Parcelable.Creator<BeanGroup> CREATOR = new Parcelable.Creator<BeanGroup>() {
        @Override
        public BeanGroup createFromParcel(Parcel source) {
            return new BeanGroup(source);
        }

        @Override
        public BeanGroup[] newArray(int size) {
            return new BeanGroup[size];
        }
    };
}
