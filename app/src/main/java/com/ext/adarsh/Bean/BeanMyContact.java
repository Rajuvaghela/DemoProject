package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 31-08-2017.
 */

public class BeanMyContact implements Parcelable {

    @SerializedName("ContactId")
    public String contactId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("PeopleFlag")
    public String peopleFlag;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("IsFavorite")
    public String isFavorite;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contactId);
        dest.writeString(this.fullName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.mobileNo);
        dest.writeString(this.peopleFlag);
        dest.writeString(this.profileImage);
        dest.writeString(this.peopleId);
        dest.writeString(this.isFavorite);
    }

    public BeanMyContact() {
    }

    protected BeanMyContact(Parcel in) {
        this.contactId = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.mobileNo = in.readString();
        this.peopleFlag = in.readString();
        this.profileImage = in.readString();
        this.peopleId = in.readString();
        this.isFavorite = in.readString();
    }

    public static final Parcelable.Creator<BeanMyContact> CREATOR = new Parcelable.Creator<BeanMyContact>() {
        @Override
        public BeanMyContact createFromParcel(Parcel source) {
            return new BeanMyContact(source);
        }

        @Override
        public BeanMyContact[] newArray(int size) {
            return new BeanMyContact[size];
        }
    };
}
