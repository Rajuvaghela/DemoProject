package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */

public class BeanRegionList implements Parcelable {
    @SerializedName("RegionId")
    public String regionId;

    @SerializedName("RegionName")
    public String regionName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.regionId);
        dest.writeString(this.regionName);
    }

    public BeanRegionList() {
    }

    public BeanRegionList(String regionId, String regionName) {
        this.regionId = regionId;
        this.regionName = regionName;
    }



    protected BeanRegionList(Parcel in) {
        this.regionId = in.readString();
        this.regionName = in.readString();
    }

    public static final Creator<BeanRegionList> CREATOR = new Creator<BeanRegionList>() {
        @Override
        public BeanRegionList createFromParcel(Parcel source) {
            return new BeanRegionList(source);
        }

        @Override
        public BeanRegionList[] newArray(int size) {
            return new BeanRegionList[size];
        }
    };
}
