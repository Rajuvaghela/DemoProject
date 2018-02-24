package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */

public class BeanDesignationList implements Parcelable {
    @SerializedName("DesignationId")
    public String designationId;

    @SerializedName("DesignationName")
    public String designationName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.designationId);
        dest.writeString(this.designationName);
    }

    public BeanDesignationList() {
    }

    public BeanDesignationList(String designationId, String designationName) {
        this.designationName = designationName;
        this.designationId = designationId;

    }

    protected BeanDesignationList(Parcel in) {
        this.designationId = in.readString();
        this.designationName = in.readString();
    }

    public static final Creator<BeanDesignationList> CREATOR = new Creator<BeanDesignationList>() {
        @Override
        public BeanDesignationList createFromParcel(Parcel source) {
            return new BeanDesignationList(source);
        }

        @Override
        public BeanDesignationList[] newArray(int size) {
            return new BeanDesignationList[size];
        }
    };
}
