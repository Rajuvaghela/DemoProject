package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 17-01-2018.
 */

public class BeamWorkEditList implements Parcelable {

    @SerializedName("WorkId")
    public String workId;

    @SerializedName("CompanyName")
    public String companyName;

    @SerializedName("Position")
    public String position;

    @SerializedName("JoinDate")
    public String joinDate;

    @SerializedName("LeftDate")
    public String leftDate;

    @SerializedName("CompanyPlace")
    public String companyPlace;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.workId);
        dest.writeString(this.companyName);
        dest.writeString(this.position);
        dest.writeString(this.joinDate);
        dest.writeString(this.leftDate);
        dest.writeString(this.companyPlace);
    }

    public BeamWorkEditList() {
    }

    protected BeamWorkEditList(Parcel in) {
        this.workId = in.readString();
        this.companyName = in.readString();
        this.position = in.readString();
        this.joinDate = in.readString();
        this.leftDate = in.readString();
        this.companyPlace = in.readString();
    }

    public static final Creator<BeamWorkEditList> CREATOR = new Creator<BeamWorkEditList>() {
        @Override
        public BeamWorkEditList createFromParcel(Parcel source) {
            return new BeamWorkEditList(source);
        }

        @Override
        public BeamWorkEditList[] newArray(int size) {
            return new BeamWorkEditList[size];
        }
    };
}
