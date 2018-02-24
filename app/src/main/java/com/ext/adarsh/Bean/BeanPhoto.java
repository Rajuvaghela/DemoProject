package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 27-10-2017.
 */

public class BeanPhoto implements Parcelable {

    @SerializedName("AlbumDetailId")
    public String albumDetailId;

    @SerializedName("AlbumId")
    public String albumId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("PostId")
    public String postId;

    @SerializedName("Description")
    public String description;

    @SerializedName("FilePath")
    public String filePath;


    public BeanPhoto() {
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
        dest.writeString(this.description);
        dest.writeString(this.filePath);
    }

    protected BeanPhoto(Parcel in) {
        this.albumDetailId = in.readString();
        this.albumId = in.readString();
        this.peopleId = in.readString();
        this.postId = in.readString();
        this.description = in.readString();
        this.filePath = in.readString();
    }

    public static final Creator<BeanPhoto> CREATOR = new Creator<BeanPhoto>() {
        @Override
        public BeanPhoto createFromParcel(Parcel source) {
            return new BeanPhoto(source);
        }

        @Override
        public BeanPhoto[] newArray(int size) {
            return new BeanPhoto[size];
        }
    };
}
