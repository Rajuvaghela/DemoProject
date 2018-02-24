package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BeanEventPeople implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("EventId")
    public String eventId;

    @SerializedName("InvitedIds")
    public String invitedIds;

    @SerializedName("InvitedPersonsName")
    public String invitedPersonsName;

    @SerializedName("GoingFlag")
    public String goingFlag;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.eventId);
        dest.writeString(this.invitedIds);
        dest.writeString(this.invitedPersonsName);
        dest.writeString(this.goingFlag);
    }

    public BeanEventPeople() {
    }

    protected BeanEventPeople(Parcel in) {
        this.peopleId = in.readString();
        this.eventId = in.readString();
        this.invitedIds = in.readString();
        this.invitedPersonsName = in.readString();
        this.goingFlag = in.readString();
    }

    public static final Parcelable.Creator<BeanEventPeople> CREATOR = new Parcelable.Creator<BeanEventPeople>() {
        @Override
        public BeanEventPeople createFromParcel(Parcel source) {
            return new BeanEventPeople(source);
        }

        @Override
        public BeanEventPeople[] newArray(int size) {
            return new BeanEventPeople[size];
        }
    };
}
