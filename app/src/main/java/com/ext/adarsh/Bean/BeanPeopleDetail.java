package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 31-08-2017.
 */

public class BeanPeopleDetail implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("Birthdate")
    public String birthdate;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("HomeNo")
    public String homeNo;

    @SerializedName("Address")
    public String address;

    public BeanPeopleDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.mobileNo);
        dest.writeString(this.birthdate);
        dest.writeString(this.profileImage);
        dest.writeString(this.homeNo);
        dest.writeString(this.address);
    }

    protected BeanPeopleDetail(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.mobileNo = in.readString();
        this.birthdate = in.readString();
        this.profileImage = in.readString();
        this.homeNo = in.readString();
        this.address = in.readString();
    }

    public static final Parcelable.Creator<BeanPeopleDetail> CREATOR = new Parcelable.Creator<BeanPeopleDetail>() {
        @Override
        public BeanPeopleDetail createFromParcel(Parcel source) {
            return new BeanPeopleDetail(source);
        }

        @Override
        public BeanPeopleDetail[] newArray(int size) {
            return new BeanPeopleDetail[size];
        }
    };
}
