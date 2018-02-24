package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/16/2017.
 */

public class BeanEventAddPeopleList implements Parcelable {


    private boolean isSelected = false;
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    @SerializedName("ContactId")
    public String contactId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("MobileNo")
    public String mobileNo;

    @SerializedName("PeopleFlag")
    public String peopleFlag;

    @SerializedName("ProfileImage")
    public String profileImage;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contactId);
        dest.writeString(this.fullName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.mobileNo);
        dest.writeString(this.peopleFlag);
        dest.writeString(this.profileImage);
    }

    public BeanEventAddPeopleList() {
    }

    protected BeanEventAddPeopleList(Parcel in) {
        this.contactId = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.mobileNo = in.readString();
        this.peopleFlag = in.readString();
        this.profileImage = in.readString();
    }

    public static final Parcelable.Creator<BeanEventAddPeopleList> CREATOR = new Parcelable.Creator<BeanEventAddPeopleList>() {
        @Override
        public BeanEventAddPeopleList createFromParcel(Parcel source) {
            return new BeanEventAddPeopleList(source);
        }

        @Override
        public BeanEventAddPeopleList[] newArray(int size) {
            return new BeanEventAddPeopleList[size];
        }
    };
}
