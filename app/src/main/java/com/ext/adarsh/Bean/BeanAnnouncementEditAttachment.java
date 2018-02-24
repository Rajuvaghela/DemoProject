package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 20-11-2017.
 */

public class BeanAnnouncementEditAttachment implements Parcelable {

    @SerializedName("AnnouncementId")
    public  String announcementId;

    @SerializedName("AttachmentId")
    public  String attachmentId;

    @SerializedName("FilePath")
    public  String filePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.announcementId);
        dest.writeString(this.attachmentId);
        dest.writeString(this.filePath);
    }

    public BeanAnnouncementEditAttachment() {
    }

    protected BeanAnnouncementEditAttachment(Parcel in) {
        this.announcementId = in.readString();
        this.attachmentId = in.readString();
        this.filePath = in.readString();
    }

    public static final Parcelable.Creator<BeanAnnouncementEditAttachment> CREATOR = new Parcelable.Creator<BeanAnnouncementEditAttachment>() {
        @Override
        public BeanAnnouncementEditAttachment createFromParcel(Parcel source) {
            return new BeanAnnouncementEditAttachment(source);
        }

        @Override
        public BeanAnnouncementEditAttachment[] newArray(int size) {
            return new BeanAnnouncementEditAttachment[size];
        }
    };
}
