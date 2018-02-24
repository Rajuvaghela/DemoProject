package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-11-2017.
 */

public class BeanProfileColleguesFriends implements Parcelable {

    @SerializedName("ContactId")
    public String contactId;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PeopleId")
    public String peopleId;
    @SerializedName("DepartmentName")
    public String departmentName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contactId);
        dest.writeString(this.fullName);
        dest.writeString(this.profileImage);
        dest.writeString(this.peopleId);
        dest.writeString(this.departmentName);
    }

    public BeanProfileColleguesFriends() {
    }

    protected BeanProfileColleguesFriends(Parcel in) {
        this.contactId = in.readString();
        this.fullName = in.readString();
        this.profileImage = in.readString();
        this.peopleId = in.readString();
        this.departmentName = in.readString();
    }

    public static final Parcelable.Creator<BeanProfileColleguesFriends> CREATOR = new Parcelable.Creator<BeanProfileColleguesFriends>() {
        @Override
        public BeanProfileColleguesFriends createFromParcel(Parcel source) {
            return new BeanProfileColleguesFriends(source);
        }

        @Override
        public BeanProfileColleguesFriends[] newArray(int size) {
            return new BeanProfileColleguesFriends[size];
        }
    };
}
