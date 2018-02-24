package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/5/2018.
 */

public class BeanFeedNewsTagFriendOne2 implements Parcelable {

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

    public BeanFeedNewsTagFriendOne2() {
    }

    protected BeanFeedNewsTagFriendOne2(Parcel in) {
        this.personsName = in.readString();
        this.personsId = in.readString();
    }

    public static final Creator<BeanFeedNewsTagFriendOne2> CREATOR = new Creator<BeanFeedNewsTagFriendOne2>() {
        @Override
        public BeanFeedNewsTagFriendOne2 createFromParcel(Parcel source) {
            return new BeanFeedNewsTagFriendOne2(source);
        }

        @Override
        public BeanFeedNewsTagFriendOne2[] newArray(int size) {
            return new BeanFeedNewsTagFriendOne2[size];
        }
    };
}
