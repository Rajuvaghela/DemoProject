package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 11-11-2017.
 */

public class BeanPeopleList implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("ProfileImage")
    public String profileImage;

    @Override
    public int describeContents() {
        return 0;
    }

    public BeanPeopleList(String peopleId, String fullName, String emailAddress, String mobileNo, String profileImage) {
        this.peopleId = peopleId;
        this.fullName = fullName;
        this.emailAddress = emailAddress;
        this.mobileNo = mobileNo;
        this.profileImage = profileImage;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.mobileNo);
        dest.writeString(this.profileImage);
    }

    public BeanPeopleList() {
    }

    protected BeanPeopleList(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.mobileNo = in.readString();
        this.profileImage = in.readString();
    }

    public static final Creator<BeanPeopleList> CREATOR = new Creator<BeanPeopleList>() {
        @Override
        public BeanPeopleList createFromParcel(Parcel source) {
            return new BeanPeopleList(source);
        }

        @Override
        public BeanPeopleList[] newArray(int size) {
            return new BeanPeopleList[size];
        }
    };
}
