package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/5/2018.
 */

public class BeanFeedNewsTagFriendMore2 implements Parcelable {

    @SerializedName("PersonsName")
    public String personsName;

    @SerializedName("PersonsId")
    public String personsId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.personsName);
        dest.writeString(this.personsId);
    }

    public BeanFeedNewsTagFriendMore2() {
    }

    protected BeanFeedNewsTagFriendMore2(Parcel in) {
        this.personsName = in.readString();
        this.personsId = in.readString();
    }

    public static final Creator<BeanFeedNewsTagFriendMore2> CREATOR = new Creator<BeanFeedNewsTagFriendMore2>() {
        @Override
        public BeanFeedNewsTagFriendMore2 createFromParcel(Parcel source) {
            return new BeanFeedNewsTagFriendMore2(source);
        }

        @Override
        public BeanFeedNewsTagFriendMore2[] newArray(int size) {
            return new BeanFeedNewsTagFriendMore2[size];
        }
    };
}
