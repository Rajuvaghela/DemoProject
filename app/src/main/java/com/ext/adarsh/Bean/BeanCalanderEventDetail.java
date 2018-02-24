package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 26-01-2018.
 */

public class BeanCalanderEventDetail implements Parcelable {

    @SerializedName("CalenderId")
    public String calenderId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("EventTitle")
    public String eventTitle;

    @SerializedName("PersonsId")
    public String personsId;

    @SerializedName("PersonsName")
    public String personsName;

    @SerializedName("StartDate")
    public String startDate;

    @SerializedName("EndDate")
    public String endDate;

    @SerializedName("StartTime")
    public String startTime;

    @SerializedName("BgColor")
    public String bgColor;

    @SerializedName("FontColor")
    public String fontColor;

    @SerializedName("Description")
    public String description;

    @SerializedName("EventType")
    public String eventType;

    @SerializedName("Address")
    public String address;
    @SerializedName("EndTime")
    public String endTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.calenderId);
        dest.writeString(this.peopleId);
        dest.writeString(this.eventTitle);
        dest.writeString(this.personsId);
        dest.writeString(this.personsName);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.startTime);
        dest.writeString(this.bgColor);
        dest.writeString(this.fontColor);
        dest.writeString(this.description);
        dest.writeString(this.eventType);
        dest.writeString(this.address);
        dest.writeString(this.endTime);
    }

    public BeanCalanderEventDetail() {
    }

    protected BeanCalanderEventDetail(Parcel in) {
        this.calenderId = in.readString();
        this.peopleId = in.readString();
        this.eventTitle = in.readString();
        this.personsId = in.readString();
        this.personsName = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.startTime = in.readString();
        this.bgColor = in.readString();
        this.fontColor = in.readString();
        this.description = in.readString();
        this.eventType = in.readString();
        this.address = in.readString();
        this.endTime = in.readString();
    }

    public static final Parcelable.Creator<BeanCalanderEventDetail> CREATOR = new Parcelable.Creator<BeanCalanderEventDetail>() {
        @Override
        public BeanCalanderEventDetail createFromParcel(Parcel source) {
            return new BeanCalanderEventDetail(source);
        }

        @Override
        public BeanCalanderEventDetail[] newArray(int size) {
            return new BeanCalanderEventDetail[size];
        }
    };
}
