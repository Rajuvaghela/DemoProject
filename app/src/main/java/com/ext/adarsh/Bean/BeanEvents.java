package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-011 on 10/3/2017.
 */

public class BeanEvents implements Parcelable {

    @SerializedName("EventId")
    public String eventId;

    @SerializedName("EventTitle")
    public String eventTitle;

    @SerializedName("EventImage")
    public String eventImage;

    @SerializedName("GoingFlag")
    public String goingFlag;

    @SerializedName("Day")
    public String day;

    @SerializedName("Month")
    public String month;

    @SerializedName("DayName")
    public String dayName;

    @SerializedName("Time")
    public String time;

    @SerializedName("Address")
    public String address;

    @SerializedName("EventType")
    public String eventType;

    @SerializedName("PublishFlag")
    public String publishFlag;

    public BeanEvents() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eventId);
        dest.writeString(this.eventTitle);
        dest.writeString(this.eventImage);
        dest.writeString(this.goingFlag);
        dest.writeString(this.day);
        dest.writeString(this.month);
        dest.writeString(this.dayName);
        dest.writeString(this.time);
        dest.writeString(this.address);
        dest.writeString(this.eventType);
        dest.writeString(this.publishFlag);
    }

    protected BeanEvents(Parcel in) {
        this.eventId = in.readString();
        this.eventTitle = in.readString();
        this.eventImage = in.readString();
        this.goingFlag = in.readString();
        this.day = in.readString();
        this.month = in.readString();
        this.dayName = in.readString();
        this.time = in.readString();
        this.address = in.readString();
        this.eventType = in.readString();
        this.publishFlag = in.readString();
    }

    public static final Creator<BeanEvents> CREATOR = new Creator<BeanEvents>() {
        @Override
        public BeanEvents createFromParcel(Parcel source) {
            return new BeanEvents(source);
        }

        @Override
        public BeanEvents[] newArray(int size) {
            return new BeanEvents[size];
        }
    };
}
