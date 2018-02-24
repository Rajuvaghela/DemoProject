package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/23/2018.
 */

public class BeanColleaguesList implements Parcelable {

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FullName")
    public String fullName;

    @Override
    public int describeContents() {
        return 0;
    }

    public BeanColleaguesList(String peopleId, String fullName) {
        this.fullName = fullName;
        this.peopleId = peopleId;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.fullName);
    }

    public BeanColleaguesList() {
    }

    protected BeanColleaguesList(Parcel in) {
        this.peopleId = in.readString();
        this.fullName = in.readString();
    }

    public static final Parcelable.Creator<BeanColleaguesList> CREATOR = new Parcelable.Creator<BeanColleaguesList>() {
        @Override
        public BeanColleaguesList createFromParcel(Parcel source) {
            return new BeanColleaguesList(source);
        }

        @Override
        public BeanColleaguesList[] newArray(int size) {
            return new BeanColleaguesList[size];
        }
    };
}
