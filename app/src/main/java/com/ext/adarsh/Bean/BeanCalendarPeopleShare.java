package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 17-01-2018.
 */

public class BeanCalendarPeopleShare implements Parcelable {

    private boolean isSelected = true;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }


    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("BgColor")
    public String bgColor;

    @SerializedName("FontColor")
    public String fontColor;


    public BeanCalendarPeopleShare() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
        dest.writeString(this.bgColor);
        dest.writeString(this.fontColor);
    }

    protected BeanCalendarPeopleShare(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
        this.bgColor = in.readString();
        this.fontColor = in.readString();
    }

    public static final Creator<BeanCalendarPeopleShare> CREATOR = new Creator<BeanCalendarPeopleShare>() {
        @Override
        public BeanCalendarPeopleShare createFromParcel(Parcel source) {
            return new BeanCalendarPeopleShare(source);
        }

        @Override
        public BeanCalendarPeopleShare[] newArray(int size) {
            return new BeanCalendarPeopleShare[size];
        }
    };
}
