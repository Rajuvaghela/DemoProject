package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BeanEventDetail implements Parcelable {

    @SerializedName("EventId")
    public String eventId;

    @SerializedName("EventTitle")
    public String eventTitle;

    @SerializedName("StartDate")
    public String startDate;

    @SerializedName("EndDate")
    public String endDate;

    @SerializedName("StartTime")
    public String startTime;

    @SerializedName("EndTime")
    public String endTime;

    @SerializedName("Address")
    public String address;

    @SerializedName("Description")
    public String description;

    @SerializedName("Latitude")
    public String latitude;

    @SerializedName("Longitude")
    public String longitude;

    @SerializedName("EventImage")
    public String eventImage;

    @SerializedName("ContactPersonName")
    public String contactPersonName;

    @SerializedName("DepartmentName")
    public String departmentName;

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("DesignationName")
    public String designationName;

    public BeanEventDetail() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventId);
        dest.writeString(this.eventTitle);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.eventImage);
        dest.writeString(this.contactPersonName);
        dest.writeString(this.departmentName);
        dest.writeString(this.mobileNo);
        dest.writeString(this.emailAddress);
        dest.writeString(this.profileImage);
        dest.writeString(this.designationName);
    }

    protected BeanEventDetail(Parcel in) {
        this.eventId = in.readString();
        this.eventTitle = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.address = in.readString();
        this.description = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.eventImage = in.readString();
        this.contactPersonName = in.readString();
        this.departmentName = in.readString();
        this.mobileNo = in.readString();
        this.emailAddress = in.readString();
        this.profileImage = in.readString();
        this.designationName = in.readString();
    }

    public static final Creator<BeanEventDetail> CREATOR = new Creator<BeanEventDetail>() {
        @Override
        public BeanEventDetail createFromParcel(Parcel source) {
            return new BeanEventDetail(source);
        }

        @Override
        public BeanEventDetail[] newArray(int size) {
            return new BeanEventDetail[size];
        }
    };
}
