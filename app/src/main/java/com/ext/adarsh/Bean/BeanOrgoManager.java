package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 09-08-2017.
 */

public class BeanOrgoManager implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("BranchName")
    public String branchName;

    @SerializedName("DesignationName")
    public String designationName;

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("ProfileImage")
    public String profileImage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
        dest.writeString(this.branchName);
        dest.writeString(this.designationName);
        dest.writeString(this.mobileNo);
        dest.writeString(this.emailAddress);
        dest.writeString(this.profileImage);
    }

    public BeanOrgoManager() {
    }

    protected BeanOrgoManager(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
        this.branchName = in.readString();
        this.designationName = in.readString();
        this.mobileNo = in.readString();
        this.emailAddress = in.readString();
        this.profileImage = in.readString();
    }

    public static final Creator<BeanOrgoManager> CREATOR = new Creator<BeanOrgoManager>() {
        @Override
        public BeanOrgoManager createFromParcel(Parcel source) {
            return new BeanOrgoManager(source);
        }

        @Override
        public BeanOrgoManager[] newArray(int size) {
            return new BeanOrgoManager[size];
        }
    };
}
