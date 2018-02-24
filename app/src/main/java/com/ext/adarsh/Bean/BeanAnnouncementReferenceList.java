package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/10/2017.
 */

public class BeanAnnouncementReferenceList implements Parcelable {

    @SerializedName("AnnouncementId")
    public String announcementId;


    @SerializedName("AnnouncementDetail")
    public String announcementDetail;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.announcementId);
        dest.writeString(this.announcementDetail);
    }

    public BeanAnnouncementReferenceList() {
    }

    public BeanAnnouncementReferenceList(String announcementId, String announcementDetail) {
        this.announcementDetail = announcementDetail;
        this.announcementId = announcementId;

    }

    protected BeanAnnouncementReferenceList(Parcel in) {
        this.announcementId = in.readString();
        this.announcementDetail = in.readString();
    }

    public static final Creator<BeanAnnouncementReferenceList> CREATOR = new Creator<BeanAnnouncementReferenceList>() {
        @Override
        public BeanAnnouncementReferenceList createFromParcel(Parcel source) {
            return new BeanAnnouncementReferenceList(source);
        }

        @Override
        public BeanAnnouncementReferenceList[] newArray(int size) {
            return new BeanAnnouncementReferenceList[size];
        }
    };
}
