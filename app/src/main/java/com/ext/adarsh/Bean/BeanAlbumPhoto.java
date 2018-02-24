package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 27-10-2017.
 */

public class BeanAlbumPhoto implements Parcelable {

    @SerializedName("AlbumDetailId")
    public String albumDetailId;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("FileType")
    public String fileType;


    @SerializedName("PostId")
    public String postId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumDetailId);
        dest.writeString(this.filePath);
        dest.writeString(this.fileType);
        dest.writeString(this.postId);
    }

    public BeanAlbumPhoto() {
    }

    protected BeanAlbumPhoto(Parcel in) {
        this.albumDetailId = in.readString();
        this.filePath = in.readString();
        this.fileType = in.readString();
        this.postId = in.readString();
    }

    public static final Creator<BeanAlbumPhoto> CREATOR = new Creator<BeanAlbumPhoto>() {
        @Override
        public BeanAlbumPhoto createFromParcel(Parcel source) {
            return new BeanAlbumPhoto(source);
        }

        @Override
        public BeanAlbumPhoto[] newArray(int size) {
            return new BeanAlbumPhoto[size];
        }
    };
}
