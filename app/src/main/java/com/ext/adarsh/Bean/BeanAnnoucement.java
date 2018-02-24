package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanAnnoucement implements Parcelable {

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

    @SerializedName("Announcement_Attachments_Array")
    public ArrayList<BeanAnnoucementAttchment> announcement_Attachments_Array;

    public BeanAnnoucement() {
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

    protected BeanAnnoucement(Parcel in) {
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

    public static final Creator<BeanAnnoucement> CREATOR = new Creator<BeanAnnoucement>() {
        @Override
        public BeanAnnoucement createFromParcel(Parcel source) {
            return new BeanAnnoucement(source);
        }

        @Override
        public BeanAnnoucement[] newArray(int size) {
            return new BeanAnnoucement[size];
        }
    };
}