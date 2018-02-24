package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanMyAnnoucement implements Parcelable {

    @SerializedName("AnnouncementId")
    public String announcementId;

    @SerializedName("AnnouncementTitle")
    public String announcementTitle;

    @SerializedName("AnnouncementDetail")
    public String announcementDetail;

    @SerializedName("Day")
    public String day;

    @SerializedName("PublishFlag")
    public String publishFlag;

    @SerializedName("isAttachment")
    public String isAttachment;

    @SerializedName("ReferenceToPost")
    public String referenceToPost;

    @SerializedName("ReferenceToPostTitle")
    public String referenceToPostTitle;

    @SerializedName("Month")
    public String month;

    @SerializedName("PublishBy")
    public String publishBy;

    @SerializedName("My_Announcement_Attachments_Array")
    public ArrayList<BeanAnnoucementAttchment> announcement_Attachments_Array;

    public BeanMyAnnoucement() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.announcementId);
        dest.writeString(this.announcementTitle);
        dest.writeString(this.announcementDetail);
        dest.writeString(this.day);
        dest.writeString(this.publishFlag);
        dest.writeString(this.isAttachment);
        dest.writeString(this.referenceToPost);
        dest.writeString(this.referenceToPostTitle);
        dest.writeString(this.month);
        dest.writeString(this.publishBy);
        dest.writeTypedList(this.announcement_Attachments_Array);
    }

    protected BeanMyAnnoucement(Parcel in) {
        this.announcementId = in.readString();
        this.announcementTitle = in.readString();
        this.announcementDetail = in.readString();
        this.day = in.readString();
        this.publishFlag = in.readString();
        this.isAttachment = in.readString();
        this.referenceToPost = in.readString();
        this.referenceToPostTitle = in.readString();
        this.month = in.readString();
        this.publishBy = in.readString();
        this.announcement_Attachments_Array = in.createTypedArrayList(BeanAnnoucementAttchment.CREATOR);
    }

    public static final Creator<BeanMyAnnoucement> CREATOR = new Creator<BeanMyAnnoucement>() {
        @Override
        public BeanMyAnnoucement createFromParcel(Parcel source) {
            return new BeanMyAnnoucement(source);
        }

        @Override
        public BeanMyAnnoucement[] newArray(int size) {
            return new BeanMyAnnoucement[size];
        }
    };
}