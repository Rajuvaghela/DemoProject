package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 25-01-2018.
 */

public class BeanPeopleFileShare implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("PeopleName")
    public String peopleName;

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
        dest.writeString(this.peopleName);
    }

    public BeanPeopleFileShare() {
    }

    protected BeanPeopleFileShare(Parcel in) {
        this.peopleId = in.readString();
        this.peopleName = in.readString();
    }

    public static final Creator<BeanPeopleFileShare> CREATOR = new Creator<BeanPeopleFileShare>() {
        @Override
        public BeanPeopleFileShare createFromParcel(Parcel source) {
            return new BeanPeopleFileShare(source);
        }

        @Override
        public BeanPeopleFileShare[] newArray(int size) {
            return new BeanPeopleFileShare[size];
        }
    };
}
