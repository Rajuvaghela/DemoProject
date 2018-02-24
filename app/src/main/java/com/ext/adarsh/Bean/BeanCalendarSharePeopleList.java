package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 11-11-2017.
 */

public class BeanCalendarSharePeopleList implements Parcelable {

    @SerializedName("ShareId")
    public String shareId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("PeopleId")
    public String peopleId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shareId);
        dest.writeString(this.fullName);
        dest.writeString(this.peopleId);
    }

    public BeanCalendarSharePeopleList() {
    }

    protected BeanCalendarSharePeopleList(Parcel in) {
        this.shareId = in.readString();
        this.fullName = in.readString();
        this.peopleId = in.readString();
    }

    public static final Creator<BeanCalendarSharePeopleList> CREATOR = new Creator<BeanCalendarSharePeopleList>() {
        @Override
        public BeanCalendarSharePeopleList createFromParcel(Parcel source) {
            return new BeanCalendarSharePeopleList(source);
        }

        @Override
        public BeanCalendarSharePeopleList[] newArray(int size) {
            return new BeanCalendarSharePeopleList[size];
        }
    };
}
