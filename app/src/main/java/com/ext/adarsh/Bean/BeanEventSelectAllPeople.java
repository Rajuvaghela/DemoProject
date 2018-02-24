package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class BeanEventSelectAllPeople implements Parcelable {

    private boolean isSelected = false;
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }


    @SerializedName("PeopleId")
    public  String peopleId;

    @SerializedName("FullName")
    public  String fullName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
    }

    public BeanEventSelectAllPeople() {
    }

    protected BeanEventSelectAllPeople(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
    }

    public static final Parcelable.Creator<BeanEventSelectAllPeople> CREATOR = new Parcelable.Creator<BeanEventSelectAllPeople>() {
        @Override
        public BeanEventSelectAllPeople createFromParcel(Parcel source) {
            return new BeanEventSelectAllPeople(source);
        }

        @Override
        public BeanEventSelectAllPeople[] newArray(int size) {
            return new BeanEventSelectAllPeople[size];
        }
    };
}
