package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/5/2018.
 */

public class BeanFeedNewsTagFriendOne implements Parcelable {

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

    public BeanFeedNewsTagFriendOne() {
    }

    protected BeanFeedNewsTagFriendOne(Parcel in) {
        this.personsName = in.readString();
        this.personsId = in.readString();
    }

    public static final Parcelable.Creator<BeanFeedNewsTagFriendOne> CREATOR = new Parcelable.Creator<BeanFeedNewsTagFriendOne>() {
        @Override
        public BeanFeedNewsTagFriendOne createFromParcel(Parcel source) {
            return new BeanFeedNewsTagFriendOne(source);
        }

        @Override
        public BeanFeedNewsTagFriendOne[] newArray(int size) {
            return new BeanFeedNewsTagFriendOne[size];
        }
    };
}
