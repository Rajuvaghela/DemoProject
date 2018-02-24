package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 9/28/2017.
 */

public class BeanProfileDetail implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FirstName")
    public String firstName;

    @SerializedName("LastName")
    public String lastName;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("DepartmentId")
    public String departmentId;

    @SerializedName("BranchId")
    public String branchId;

    @SerializedName("CountryId")
    public String countryId;

    @SerializedName("StateId")
    public String stateId;

    @SerializedName("CityId")
    public String cityId;

    @SerializedName("RegionId")
    public String regionId;

    @SerializedName("PeopleFlag")
    public String peopleFlag;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.fullName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.mobileNo);
        dest.writeString(this.departmentId);
        dest.writeString(this.branchId);
        dest.writeString(this.countryId);
        dest.writeString(this.stateId);
        dest.writeString(this.cityId);
        dest.writeString(this.regionId);
        dest.writeString(this.peopleFlag);
    }

    public BeanProfileDetail() {
    }

    protected BeanProfileDetail(Parcel in) {
        this.peopleId = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.mobileNo = in.readString();
        this.departmentId = in.readString();
        this.branchId = in.readString();
        this.countryId = in.readString();
        this.stateId = in.readString();
        this.cityId = in.readString();
        this.regionId = in.readString();
        this.peopleFlag = in.readString();
    }

    public static final Parcelable.Creator<BeanProfileDetail> CREATOR = new Parcelable.Creator<BeanProfileDetail>() {
        @Override
        public BeanProfileDetail createFromParcel(Parcel source) {
            return new BeanProfileDetail(source);
        }

        @Override
        public BeanProfileDetail[] newArray(int size) {
            return new BeanProfileDetail[size];
        }
    };
}
