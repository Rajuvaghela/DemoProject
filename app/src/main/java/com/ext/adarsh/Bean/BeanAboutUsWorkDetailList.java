package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/7/2017.
 */

public class BeanAboutUsWorkDetailList implements Parcelable {

    @SerializedName("WorkId")
    public String workId;

    @SerializedName("CompanyName")
    public String companyName;

    @SerializedName("Position")
    public String position;

    @SerializedName("StartPresent")
    public String startPresent;

    @SerializedName("EndPresent")
    public String endPresent;

    @SerializedName("CompanyPlace")
    public String companyPlace;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.workId);
        dest.writeString(this.companyName);
        dest.writeString(this.position);
        dest.writeString(this.startPresent);
        dest.writeString(this.endPresent);
        dest.writeString(this.companyPlace);
    }

    public BeanAboutUsWorkDetailList() {
    }

    protected BeanAboutUsWorkDetailList(Parcel in) {
        this.workId = in.readString();
        this.companyName = in.readString();
        this.position = in.readString();
        this.startPresent = in.readString();
        this.endPresent = in.readString();
        this.companyPlace = in.readString();
    }

    public static final Parcelable.Creator<BeanAboutUsWorkDetailList> CREATOR = new Parcelable.Creator<BeanAboutUsWorkDetailList>() {
        @Override
        public BeanAboutUsWorkDetailList createFromParcel(Parcel source) {
            return new BeanAboutUsWorkDetailList(source);
        }

        @Override
        public BeanAboutUsWorkDetailList[] newArray(int size) {
            return new BeanAboutUsWorkDetailList[size];
        }
    };
}
