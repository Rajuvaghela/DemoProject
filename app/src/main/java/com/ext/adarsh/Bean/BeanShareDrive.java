package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/**
 * Created by ExT-Emp-011 on 2/2/2018.
 */
public class BeanShareDrive implements Parcelable {

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

    @SerializedName("PrivecyFlag")
    public String privecyFlag;

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
        dest.writeString(this.privecyFlag);
    }

    public BeanShareDrive() {
    }

    protected BeanShareDrive(Parcel in) {
        this.fileId = in.readString();
        this.parentId = in.readString();
        this.folderName = in.readString();
        this.isFile = in.readString();
        this.icon = in.readString();
        this.memberCount = in.readString();
        this.filePath = in.readString();
        this.shareWithMeFlag = in.readString();
        this.privecyFlag = in.readString();
    }

    public static final Parcelable.Creator<BeanShareDrive> CREATOR = new Parcelable.Creator<BeanShareDrive>() {
        @Override
        public BeanShareDrive createFromParcel(Parcel source) {
            return new BeanShareDrive(source);
        }

        @Override
        public BeanShareDrive[] newArray(int size) {
            return new BeanShareDrive[size];
        }
    };
}
