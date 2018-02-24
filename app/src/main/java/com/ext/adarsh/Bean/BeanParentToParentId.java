package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/30/2018.
 */

public class BeanParentToParentId implements Parcelable {
    @SerializedName("ParentIdNew")
    public String parentIdNew;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.parentIdNew);
    }

    public BeanParentToParentId() {
    }

    protected BeanParentToParentId(Parcel in) {
        this.parentIdNew = in.readString();
    }

    public static final Parcelable.Creator<BeanParentToParentId> CREATOR = new Parcelable.Creator<BeanParentToParentId>() {
        @Override
        public BeanParentToParentId createFromParcel(Parcel source) {
            return new BeanParentToParentId(source);
        }

        @Override
        public BeanParentToParentId[] newArray(int size) {
            return new BeanParentToParentId[size];
        }
    };
}
