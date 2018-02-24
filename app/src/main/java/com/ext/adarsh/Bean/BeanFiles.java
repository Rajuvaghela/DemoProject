package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanFiles implements Parcelable {

    @SerializedName("FileId")
    public String fileId;

    @SerializedName("ParentId")
    public String parentId;

    @SerializedName("FolderName")
    public String folderName;

    @SerializedName("IsFile")
    public String isFile;

    @SerializedName("Icon")
    public String icon;

    @SerializedName("MemberCount")
    public String memberCount;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("ShareWithMeFlag")
    public String shareWithMeFlag;

    @SerializedName("FilePath1")
    public String filePath1;

    @SerializedName("FullPath")
    public String fullPath;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileId);
        dest.writeString(this.parentId);
        dest.writeString(this.folderName);
        dest.writeString(this.isFile);
        dest.writeString(this.icon);
        dest.writeString(this.memberCount);
        dest.writeString(this.filePath);
        dest.writeString(this.shareWithMeFlag);
        dest.writeString(this.filePath1);
        dest.writeString(this.fullPath);
    }

    public BeanFiles() {
    }

    protected BeanFiles(Parcel in) {
        this.fileId = in.readString();
        this.parentId = in.readString();
        this.folderName = in.readString();
        this.isFile = in.readString();
        this.icon = in.readString();
        this.memberCount = in.readString();
        this.filePath = in.readString();
        this.shareWithMeFlag = in.readString();
        this.filePath1 = in.readString();
        this.fullPath = in.readString();
    }

    public static final Parcelable.Creator<BeanFiles> CREATOR = new Parcelable.Creator<BeanFiles>() {
        @Override
        public BeanFiles createFromParcel(Parcel source) {
            return new BeanFiles(source);
        }

        @Override
        public BeanFiles[] newArray(int size) {
            return new BeanFiles[size];
        }
    };
}