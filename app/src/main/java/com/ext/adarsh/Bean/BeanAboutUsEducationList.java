package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/7/2017.
 */

public class BeanAboutUsEducationList implements Parcelable {

    @SerializedName("EducationId")
    public  String educationId;

    @SerializedName("InstitiuteName")
    public  String institiuteName;

    @SerializedName("Yaer")
    public  String yaer;

    @SerializedName("Degree")
    public  String degree;

    @SerializedName("InstitiutePlace")
    public  String institiutePlace;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.educationId);
        dest.writeString(this.institiuteName);
        dest.writeString(this.yaer);
        dest.writeString(this.degree);
        dest.writeString(this.institiutePlace);
    }

    public BeanAboutUsEducationList() {
    }

    protected BeanAboutUsEducationList(Parcel in) {
        this.educationId = in.readString();
        this.institiuteName = in.readString();
        this.yaer = in.readString();
        this.degree = in.readString();
        this.institiutePlace = in.readString();
    }

    public static final Parcelable.Creator<BeanAboutUsEducationList> CREATOR = new Parcelable.Creator<BeanAboutUsEducationList>() {
        @Override
        public BeanAboutUsEducationList createFromParcel(Parcel source) {
            return new BeanAboutUsEducationList(source);
        }

        @Override
        public BeanAboutUsEducationList[] newArray(int size) {
            return new BeanAboutUsEducationList[size];
        }
    };
}
