package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 26-01-2018.
 */

public class BeanCalanderPeople implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FullName")
    public String fullName;
    private boolean isSelected = false;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
    }

    public BeanCalanderPeople() {
    }

    protected BeanCalanderPeople(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
    }

    public static final Creator<BeanCalanderPeople> CREATOR = new Creator<BeanCalanderPeople>() {
        @Override
        public BeanCalanderPeople createFromParcel(Parcel source) {
            return new BeanCalanderPeople(source);
        }

        @Override
        public BeanCalanderPeople[] newArray(int size) {
            return new BeanCalanderPeople[size];
        }
    };
}
