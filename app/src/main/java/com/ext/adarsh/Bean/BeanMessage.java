package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanMessage implements Parcelable {
    @SerializedName("MessageId")
    public  String messageId;

    @SerializedName("FromPeopleId")
    public String fromPeopleId;

    @SerializedName("FromName")
    public String fromName;

    @SerializedName("Time")
    public String time;

    @SerializedName("IsFavourite")
    public String isFavourite;

    @SerializedName("IsRead")
    public String isRead;

    @SerializedName("ToPeopleIds")
    public String toPeopleIds;

    @SerializedName("ToPeopleNames")
    public String toPeopleNames;

    @SerializedName("ToPeopleEmails")
    public String toPeopleEmails;

    @SerializedName("Recipients")
    public String recipients;

    @SerializedName("Subject")
    public String subject;

    @SerializedName("Body")
    public String body;

    @SerializedName("AttachmentFlag")
    public String attachmentFlag;

    @SerializedName("ParentId")
    public String parentId;

    @SerializedName("Date")
    public String date;

    @SerializedName("NRFFlag")
    public String nRFFlag;


    public BeanMessage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageId);
        dest.writeString(this.fromPeopleId);
        dest.writeString(this.fromName);
        dest.writeString(this.time);
        dest.writeString(this.isFavourite);
        dest.writeString(this.isRead);
        dest.writeString(this.toPeopleIds);
        dest.writeString(this.toPeopleNames);
        dest.writeString(this.toPeopleEmails);
        dest.writeString(this.recipients);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeString(this.attachmentFlag);
        dest.writeString(this.parentId);
        dest.writeString(this.date);
        dest.writeString(this.nRFFlag);
    }

    protected BeanMessage(Parcel in) {
        this.messageId = in.readString();
        this.fromPeopleId = in.readString();
        this.fromName = in.readString();
        this.time = in.readString();
        this.isFavourite = in.readString();
        this.isRead = in.readString();
        this.toPeopleIds = in.readString();
        this.toPeopleNames = in.readString();
        this.toPeopleEmails = in.readString();
        this.recipients = in.readString();
        this.subject = in.readString();
        this.body = in.readString();
        this.attachmentFlag = in.readString();
        this.parentId = in.readString();
        this.date = in.readString();
        this.nRFFlag = in.readString();
    }

    public static final Creator<BeanMessage> CREATOR = new Creator<BeanMessage>() {
        @Override
        public BeanMessage createFromParcel(Parcel source) {
            return new BeanMessage(source);
        }

        @Override
        public BeanMessage[] newArray(int size) {
            return new BeanMessage[size];
        }
    };
}
