package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 11-01-2018.
 */

public class BeanPeopleGroups implements Parcelable {


    @SerializedName("CategoryId")
    public  String categoryId;

    @SerializedName("CategoryName")
    public  String categoryName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryName);
    }

    public BeanPeopleGroups() {
    }

    protected BeanPeopleGroups(Parcel in) {
        this.categoryId = in.readString();
        this.categoryName = in.readString();
    }

    public static final Creator<BeanPeopleGroups> CREATOR = new Creator<BeanPeopleGroups>() {
        @Override
        public BeanPeopleGroups createFromParcel(Parcel source) {
            return new BeanPeopleGroups(source);
        }

        @Override
        public BeanPeopleGroups[] newArray(int size) {
            return new BeanPeopleGroups[size];
        }
    };
}
