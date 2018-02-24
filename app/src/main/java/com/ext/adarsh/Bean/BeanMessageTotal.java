package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanMessageTotal implements Parcelable {

    @SerializedName("Total")
    public String total;



    public BeanMessageTotal() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.total);

    }

    protected BeanMessageTotal(Parcel in) {
        this.total = in.readString();

    }

    public static final Creator<BeanMessageTotal> CREATOR = new Creator<BeanMessageTotal>() {
        @Override
        public BeanMessageTotal createFromParcel(Parcel source) {
            return new BeanMessageTotal(source);
        }

        @Override
        public BeanMessageTotal[] newArray(int size) {
            return new BeanMessageTotal[size];
        }
    };
}
