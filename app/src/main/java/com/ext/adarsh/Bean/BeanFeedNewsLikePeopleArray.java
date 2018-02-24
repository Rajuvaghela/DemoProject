package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/24/2018.
 */

public class BeanFeedNewsLikePeopleArray implements Parcelable {

    @SerializedName("LikePeopleName")
    public String likePeopleName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.likePeopleName);
    }

    public BeanFeedNewsLikePeopleArray() {
    }

    protected BeanFeedNewsLikePeopleArray(Parcel in) {
        this.likePeopleName = in.readString();
    }

    public static final Parcelable.Creator<BeanFeedNewsLikePeopleArray> CREATOR = new Parcelable.Creator<BeanFeedNewsLikePeopleArray>() {
        @Override
        public BeanFeedNewsLikePeopleArray createFromParcel(Parcel source) {
            return new BeanFeedNewsLikePeopleArray(source);
        }

        @Override
        public BeanFeedNewsLikePeopleArray[] newArray(int size) {
            return new BeanFeedNewsLikePeopleArray[size];
        }
    };
}
