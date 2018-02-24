package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 23-01-2018.
 */

public class BeanCalenderEventArray implements Parcelable {

    @SerializedName("NameOfToDo")
    public String nameOfToDo;

    @SerializedName("DueDate")
    public String dueDate;

    @SerializedName("BgColor")
    public String bgColor;

    @SerializedName("FontColor")
    public String fontColor;

    @SerializedName("PersonId")
    public String personId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("Id")
    public String id;

    @SerializedName("EventType")
    public String eventType;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nameOfToDo);
        dest.writeString(this.dueDate);
        dest.writeString(this.bgColor);
        dest.writeString(this.fontColor);
        dest.writeString(this.personId);
        dest.writeString(this.peopleId);
        dest.writeString(this.id);
        dest.writeString(this.eventType);
    }

    public BeanCalenderEventArray() {
    }

    protected BeanCalenderEventArray(Parcel in) {
        this.nameOfToDo = in.readString();
        this.dueDate = in.readString();
        this.bgColor = in.readString();
        this.fontColor = in.readString();
        this.personId = in.readString();
        this.peopleId = in.readString();
        this.id = in.readString();
        this.eventType = in.readString();
    }

    public static final Creator<BeanCalenderEventArray> CREATOR = new Creator<BeanCalenderEventArray>() {
        @Override
        public BeanCalenderEventArray createFromParcel(Parcel source) {
            return new BeanCalenderEventArray(source);
        }

        @Override
        public BeanCalenderEventArray[] newArray(int size) {
            return new BeanCalenderEventArray[size];
        }
    };
}
