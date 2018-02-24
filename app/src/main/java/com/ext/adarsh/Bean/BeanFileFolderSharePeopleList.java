package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-008 on 25-01-2018.
 */

public class BeanFileFolderSharePeopleList implements Parcelable {

    @SerializedName("ShareId")
    public String shareId;

    @SerializedName("ProfileImage")
    public String profileImage;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("FileId")
    public String fileId;

    @SerializedName("PrivecyFlag")
    public String privecyFlag;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("EmailAddress")
    public String emailAddress;

    @SerializedName("FileFolderName")
    public String fileFolderName;

    @SerializedName("MemberCount")
    public String memberCount;

    @SerializedName("PrivecyFlagTitle")
    public String privecyFlagTitle;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shareId);
        dest.writeString(this.profileImage);
        dest.writeString(this.peopleId);
        dest.writeString(this.fileId);
        dest.writeString(this.privecyFlag);
        dest.writeString(this.fullName);
        dest.writeString(this.emailAddress);
        dest.writeString(this.fileFolderName);
        dest.writeString(this.memberCount);
        dest.writeString(this.privecyFlagTitle);
    }

    public BeanFileFolderSharePeopleList() {
    }

    protected BeanFileFolderSharePeopleList(Parcel in) {
        this.shareId = in.readString();
        this.profileImage = in.readString();
        this.peopleId = in.readString();
        this.fileId = in.readString();
        this.privecyFlag = in.readString();
        this.fullName = in.readString();
        this.emailAddress = in.readString();
        this.fileFolderName = in.readString();
        this.memberCount = in.readString();
        this.privecyFlagTitle = in.readString();
    }

    public static final Creator<BeanFileFolderSharePeopleList> CREATOR = new Creator<BeanFileFolderSharePeopleList>() {
        @Override
        public BeanFileFolderSharePeopleList createFromParcel(Parcel source) {
            return new BeanFileFolderSharePeopleList(source);
        }

        @Override
        public BeanFileFolderSharePeopleList[] newArray(int size) {
            return new BeanFileFolderSharePeopleList[size];
        }
    };
}
