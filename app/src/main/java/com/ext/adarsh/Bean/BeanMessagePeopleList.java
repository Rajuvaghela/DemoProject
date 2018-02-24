package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/31/2018.
 */

public class BeanMessagePeopleList implements Parcelable {


    private boolean isSelected = false;
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }


    @SerializedName("FullNameEmail")
    public  String fullNameEmail;

    @SerializedName("FullName")
    public  String fullName;

    @SerializedName("PeopleId")
    public  String peopleId;

    @SerializedName("EmailAddress")
    public  String emailAddress;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullNameEmail);
        dest.writeString(this.fullName);
        dest.writeString(this.peopleId);
        dest.writeString(this.emailAddress);
    }

    public BeanMessagePeopleList() {
    }

    protected BeanMessagePeopleList(Parcel in) {
        this.fullNameEmail = in.readString();
        this.fullName = in.readString();
        this.peopleId = in.readString();
        this.emailAddress = in.readString();
    }

    public static final Parcelable.Creator<BeanMessagePeopleList> CREATOR = new Parcelable.Creator<BeanMessagePeopleList>() {
        @Override
        public BeanMessagePeopleList createFromParcel(Parcel source) {
            return new BeanMessagePeopleList(source);
        }

        @Override
        public BeanMessagePeopleList[] newArray(int size) {
            return new BeanMessagePeopleList[size];
        }
    };
}
