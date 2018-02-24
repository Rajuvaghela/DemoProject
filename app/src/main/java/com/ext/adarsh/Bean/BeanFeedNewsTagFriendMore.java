package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/5/2018.
 */

public class BeanFeedNewsTagFriendMore implements Parcelable {

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

    public BeanFeedNewsTagFriendMore() {
    }

    protected BeanFeedNewsTagFriendMore(Parcel in) {
        this.personsName = in.readString();
        this.personsId = in.readString();
    }

    public static final Parcelable.Creator<BeanFeedNewsTagFriendMore> CREATOR = new Parcelable.Creator<BeanFeedNewsTagFriendMore>() {
        @Override
        public BeanFeedNewsTagFriendMore createFromParcel(Parcel source) {
            return new BeanFeedNewsTagFriendMore(source);
        }

        @Override
        public BeanFeedNewsTagFriendMore[] newArray(int size) {
            return new BeanFeedNewsTagFriendMore[size];
        }
    };
}
