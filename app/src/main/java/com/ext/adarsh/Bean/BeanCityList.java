package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */

public class BeanCityList implements Parcelable {
    @SerializedName("CityId")
    public String cityId;

    @SerializedName("CityName")
    public String cityName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityId);
        dest.writeString(this.cityName);
    }

    public BeanCityList() {
    }

    public BeanCityList(String cityId, String cityName) {
        this.cityName = cityName;
        this.cityId = cityId;

    }

    protected BeanCityList(Parcel in) {
        this.cityId = in.readString();
        this.cityName = in.readString();
    }

    public static final Creator<BeanCityList> CREATOR = new Creator<BeanCityList>() {
        @Override
        public BeanCityList createFromParcel(Parcel source) {
            return new BeanCityList(source);
        }

        @Override
        public BeanCityList[] newArray(int size) {
            return new BeanCityList[size];
        }
    };
}
