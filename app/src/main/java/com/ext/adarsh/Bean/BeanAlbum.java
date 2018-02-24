package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 09-08-2017.
 */

public class BeanAlbum implements Parcelable {

    @SerializedName("AlbumId")
    public String albumId;

    @SerializedName("AlbumName")
    public String albumName;

    @SerializedName("TotalPhotos")
    public String totalPhotos;

    @SerializedName("FilePath")
    public String filePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumId);
        dest.writeString(this.albumName);
        dest.writeString(this.totalPhotos);
        dest.writeString(this.filePath);
    }

    public BeanAlbum(String albumId,String albumName,String totalPhotos,String filePath) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.totalPhotos = totalPhotos;
        this.filePath = filePath;
    }


    protected BeanAlbum(Parcel in) {
        this.albumId = in.readString();
        this.albumName = in.readString();
        this.totalPhotos = in.readString();
        this.filePath = in.readString();
    }

    public static final Parcelable.Creator<BeanAlbum> CREATOR = new Parcelable.Creator<BeanAlbum>() {
        @Override
        public BeanAlbum createFromParcel(Parcel source) {
            return new BeanAlbum(source);
        }

        @Override
        public BeanAlbum[] newArray(int size) {
            return new BeanAlbum[size];
        }
    };
}
