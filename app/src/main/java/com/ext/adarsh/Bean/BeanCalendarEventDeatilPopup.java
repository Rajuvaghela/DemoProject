package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 09-08-2017.
 */

public class BeanCalendarEventDeatilPopup implements Parcelable {

    @SerializedName("CalenderId")
    public String calenderId;

    @SerializedName("EventTitle")
    public String eventTitle;

    @SerializedName("StartDate")
    public String startDate;

    @SerializedName("EndDate")
    public String endDate;

    @SerializedName("StartTime")
    public String startTime;

    @SerializedName("PersonsName")
    public String personsName;

    @SerializedName("Address")
    public String address;

    @SerializedName("Description")
    public String description;

    @SerializedName("EndTime")
    public String endTime;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.calenderId);
        dest.writeString(this.eventTitle);
        dest.writeString(this.startDate);
        dest.writeString(this.endDate);
        dest.writeString(this.startTime);
        dest.writeString(this.personsName);
        dest.writeString(this.address);
        dest.writeString(this.description);
        dest.writeString(this.endTime);
    }

    public BeanCalendarEventDeatilPopup() {
    }

    protected BeanCalendarEventDeatilPopup(Parcel in) {
        this.calenderId = in.readString();
        this.eventTitle = in.readString();
        this.startDate = in.readString();
        this.endDate = in.readString();
        this.startTime = in.readString();
        this.personsName = in.readString();
        this.address = in.readString();
        this.description = in.readString();
        this.endTime = in.readString();
    }

    public static final Parcelable.Creator<BeanCalendarEventDeatilPopup> CREATOR = new Parcelable.Creator<BeanCalendarEventDeatilPopup>() {
        @Override
        public BeanCalendarEventDeatilPopup createFromParcel(Parcel source) {
            return new BeanCalendarEventDeatilPopup(source);
        }

        @Override
        public BeanCalendarEventDeatilPopup[] newArray(int size) {
            return new BeanCalendarEventDeatilPopup[size];
        }
    };
}
