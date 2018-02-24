package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 2/3/2018.
 */

public class BeanFeddPostImage implements Parcelable {

    @SerializedName("AlbumDetailId")
    public String albumDetailId;


    @SerializedName("AlbumId")
    public String albumId;


    @SerializedName("PostId")
    public String postId;


    @SerializedName("Description")
    public String description;


    @SerializedName("FilePath")
    public String filePath;


    @SerializedName("FileType")
    public String fileType;


    @SerializedName("PrivacyFlag")
    public String privacyFlag;


    @SerializedName("PostDate")
    public String postDate;

    @SerializedName("PostTime")
    public String postTime;

    @SerializedName("LikeIds")
    public String likeIds;

    @SerializedName("NotificationFlag")
    public String notificationFlag;

    @SerializedName("PinFlag")
    public String pinFlag;

    @SerializedName("NoImageFlag")
    public String noImageFlag;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumDetailId);
        dest.writeString(this.albumId);
        dest.writeString(this.postId);
        dest.writeString(this.description);
        dest.writeString(this.filePath);
        dest.writeString(this.fileType);
        dest.writeString(this.privacyFlag);
        dest.writeString(this.postDate);
        dest.writeString(this.postTime);
        dest.writeString(this.likeIds);
        dest.writeString(this.notificationFlag);
        dest.writeString(this.pinFlag);
        dest.writeString(this.noImageFlag);
    }

    public BeanFeddPostImage() {
    }

    protected BeanFeddPostImage(Parcel in) {
        this.albumDetailId = in.readString();
        this.albumId = in.readString();
        this.postId = in.readString();
        this.description = in.readString();
        this.filePath = in.readString();
        this.fileType = in.readString();
        this.privacyFlag = in.readString();
        this.postDate = in.readString();
        this.postTime = in.readString();
        this.likeIds = in.readString();
        this.notificationFlag = in.readString();
        this.pinFlag = in.readString();
        this.noImageFlag = in.readString();
    }

    public static final Parcelable.Creator<BeanFeddPostImage> CREATOR = new Parcelable.Creator<BeanFeddPostImage>() {
        @Override
        public BeanFeddPostImage createFromParcel(Parcel source) {
            return new BeanFeddPostImage(source);
        }

        @Override
        public BeanFeddPostImage[] newArray(int size) {
            return new BeanFeddPostImage[size];
        }
    };
}
