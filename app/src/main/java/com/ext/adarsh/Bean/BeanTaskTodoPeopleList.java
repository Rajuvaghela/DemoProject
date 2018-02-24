package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/25/2017.
 */

public class BeanTaskTodoPeopleList implements Parcelable {


    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FullName")
    public String fullName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
    }

    public BeanTaskTodoPeopleList(String peopleId, String fullName) {
        this.fullName = fullName;
        this.peopleId = peopleId;
    }

    public BeanTaskTodoPeopleList() {
    }

    protected BeanTaskTodoPeopleList(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
    }

    public static final Parcelable.Creator<BeanTaskTodoPeopleList> CREATOR = new Parcelable.Creator<BeanTaskTodoPeopleList>() {
        @Override
        public BeanTaskTodoPeopleList createFromParcel(Parcel source) {
            return new BeanTaskTodoPeopleList(source);
        }

        @Override
        public BeanTaskTodoPeopleList[] newArray(int size) {
            return new BeanTaskTodoPeopleList[size];
        }
    };
}
