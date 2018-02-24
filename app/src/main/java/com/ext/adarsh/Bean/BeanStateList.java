package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */

public class BeanStateList implements Parcelable {
    @SerializedName("StateId")
    public String stateId;


    @SerializedName("StateName")
    public String stateName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stateId);
        dest.writeString(this.stateName);
    }

    public BeanStateList() {

    }

    public BeanStateList(String stateId, String stateName) {
        this.stateName = stateName;
        this.stateId = stateId;

    }

    protected BeanStateList(Parcel in) {
        this.stateId = in.readString();
        this.stateName = in.readString();
    }

    public static final Creator<BeanStateList> CREATOR = new Creator<BeanStateList>() {
        @Override
        public BeanStateList createFromParcel(Parcel source) {
            return new BeanStateList(source);
        }

        @Override
        public BeanStateList[] newArray(int size) {
            return new BeanStateList[size];
        }
    };
}
