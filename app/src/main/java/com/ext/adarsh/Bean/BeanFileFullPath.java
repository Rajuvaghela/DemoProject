package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 01-12-2017.
 */

public class BeanFileFullPath implements Parcelable {


    @SerializedName("FullPath")
    public  String fullPath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullPath);
    }

    public BeanFileFullPath() {
    }

    protected BeanFileFullPath(Parcel in) {
        this.fullPath = in.readString();
    }

    public static final Parcelable.Creator<BeanFileFullPath> CREATOR = new Parcelable.Creator<BeanFileFullPath>() {
        @Override
        public BeanFileFullPath createFromParcel(Parcel source) {
            return new BeanFileFullPath(source);
        }

        @Override
        public BeanFileFullPath[] newArray(int size) {
            return new BeanFileFullPath[size];
        }
    };
}
