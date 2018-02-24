package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */

public class BeanNotifiactionlist implements Parcelable {

    @SerializedName("ProfileImage")
    public String ProfileImage;

    @SerializedName("FullName")
    public String FullName;

    @SerializedName("NotificationMessage")
    public String NotificationMessage;

    @SerializedName("PostedTime")
    public String PostedTime;

    @SerializedName("NotificationId")
    public String NotificationId;

    @SerializedName("IsRead")
    public String IsRead;

    @SerializedName("Link")
    public String Link;

    @SerializedName("ModuleFlag")
    public String ModuleFlag;

    @SerializedName("ByPeopleId")
    public String ByPeopleId;

    @SerializedName("ToPeopleId")
    public String ToPeopleId;

    @SerializedName("Date")
    public String Date;

    @SerializedName("Time")
    public String Time;


    public BeanNotifiactionlist() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ProfileImage);
        dest.writeString(this.FullName);
        dest.writeString(this.NotificationMessage);
        dest.writeString(this.PostedTime);
        dest.writeString(this.NotificationId);
        dest.writeString(this.IsRead);
        dest.writeString(this.Link);
        dest.writeString(this.ModuleFlag);
        dest.writeString(this.ByPeopleId);
        dest.writeString(this.ToPeopleId);
        dest.writeString(this.Date);
        dest.writeString(this.Time);
    }

    protected BeanNotifiactionlist(Parcel in) {
        this.ProfileImage = in.readString();
        this.FullName = in.readString();
        this.NotificationMessage = in.readString();
        this.PostedTime = in.readString();
        this.NotificationId = in.readString();
        this.IsRead = in.readString();
        this.Link = in.readString();
        this.ModuleFlag = in.readString();
        this.ByPeopleId = in.readString();
        this.ToPeopleId = in.readString();
        this.Date = in.readString();
        this.Time = in.readString();
    }

    public static final Creator<BeanNotifiactionlist> CREATOR = new Creator<BeanNotifiactionlist>() {
        @Override
        public BeanNotifiactionlist createFromParcel(Parcel source) {
            return new BeanNotifiactionlist(source);
        }

        @Override
        public BeanNotifiactionlist[] newArray(int size) {
            return new BeanNotifiactionlist[size];
        }
    };
}
