package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanAnnoucementAttchment implements Parcelable {

    @SerializedName("AttachmentId")
    public String attachmentId;

    @SerializedName("AnnouncementId")
    public String announcementId;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("FileName")
    public String fileName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.attachmentId);
        dest.writeString(this.announcementId);
        dest.writeString(this.filePath);
        dest.writeString(this.fileName);
    }

    public BeanAnnoucementAttchment() {
    }

    protected BeanAnnoucementAttchment(Parcel in) {
        this.attachmentId = in.readString();
        this.announcementId = in.readString();
        this.filePath = in.readString();
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<BeanAnnoucementAttchment> CREATOR = new Parcelable.Creator<BeanAnnoucementAttchment>() {
        @Override
        public BeanAnnoucementAttchment createFromParcel(Parcel source) {
            return new BeanAnnoucementAttchment(source);
        }

        @Override
        public BeanAnnoucementAttchment[] newArray(int size) {
            return new BeanAnnoucementAttchment[size];
        }
    };
}