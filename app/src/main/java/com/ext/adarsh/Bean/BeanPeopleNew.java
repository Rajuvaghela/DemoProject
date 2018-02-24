package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 10-08-2017.
 */

public class BeanPeopleNew implements Parcelable {

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


    public BeanPeopleNew() {
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
        dest.writeString(this.profileImage);
    }

    protected BeanPeopleNew(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.mobileNo = in.readString();
        this.profileImage = in.readString();
    }

    public static final Creator<BeanPeopleNew> CREATOR = new Creator<BeanPeopleNew>() {
        @Override
        public BeanPeopleNew createFromParcel(Parcel source) {
            return new BeanPeopleNew(source);
        }

        @Override
        public BeanPeopleNew[] newArray(int size) {
            return new BeanPeopleNew[size];
        }
    };
}
