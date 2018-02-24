package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class BeanPhotoDetail implements Parcelable {

    @SerializedName("AlbumDetailId")
    public String albumDetailId;

    @SerializedName("AlbumId")
    public String albumId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("PostId")
    public String postId;

    @SerializedName("PostDateTime")
    public String postDateTime;

    @SerializedName("LikeCount")
    public String likeCount;

    @SerializedName("TotalComment")
    public String totalComment;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("PhotoLikeDislikeFlag")
    public String photoLikeDislikeFlag;

    public BeanPhotoDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumDetailId);
        dest.writeString(this.albumId);
        dest.writeString(this.peopleId);
        dest.writeString(this.postId);
        dest.writeString(this.postDateTime);
        dest.writeString(this.likeCount);
        dest.writeString(this.totalComment);
        dest.writeString(this.filePath);
        dest.writeString(this.photoLikeDislikeFlag);
    }

    protected BeanPhotoDetail(Parcel in) {
        this.albumDetailId = in.readString();
        this.albumId = in.readString();
        this.peopleId = in.readString();
        this.postId = in.readString();
        this.postDateTime = in.readString();
        this.likeCount = in.readString();
        this.totalComment = in.readString();
        this.filePath = in.readString();
        this.photoLikeDislikeFlag = in.readString();
    }

    public static final Creator<BeanPhotoDetail> CREATOR = new Creator<BeanPhotoDetail>() {
        @Override
        public BeanPhotoDetail createFromParcel(Parcel source) {
            return new BeanPhotoDetail(source);
        }

        @Override
        public BeanPhotoDetail[] newArray(int size) {
            return new BeanPhotoDetail[size];
        }
    };
}
