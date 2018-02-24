package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 10-08-2017.
 */

public class BeanPeoplefavourite implements Parcelable {

    @SerializedName("FavId")
    public String favId;

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

    public BeanPeoplefavourite() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.favId);
        dest.writeString(this.fullName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.mobileNo);
        dest.writeString(this.peopleFlag);
        dest.writeString(this.profileImage);
        dest.writeString(this.peopleId);
    }

    protected BeanPeoplefavourite(Parcel in) {
        this.favId = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.mobileNo = in.readString();
        this.peopleFlag = in.readString();
        this.profileImage = in.readString();
        this.peopleId = in.readString();
    }

    public static final Creator<BeanPeoplefavourite> CREATOR = new Creator<BeanPeoplefavourite>() {
        @Override
        public BeanPeoplefavourite createFromParcel(Parcel source) {
            return new BeanPeoplefavourite(source);
        }

        @Override
        public BeanPeoplefavourite[] newArray(int size) {
            return new BeanPeoplefavourite[size];
        }
    };
}
