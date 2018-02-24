package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanMessagePagination implements Parcelable {

    @SerializedName("Pagination")
    public String pagination;



    public BeanMessagePagination() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pagination);

    }

    protected BeanMessagePagination(Parcel in) {
        this.pagination = in.readString();

    }

    public static final Creator<BeanMessagePagination> CREATOR = new Creator<BeanMessagePagination>() {
        @Override
        public BeanMessagePagination createFromParcel(Parcel source) {
            return new BeanMessagePagination(source);
        }

        @Override
        public BeanMessagePagination[] newArray(int size) {
            return new BeanMessagePagination[size];
        }
    };
}
