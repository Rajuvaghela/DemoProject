package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanMessageDetail implements Parcelable {
    @SerializedName("MessageId")
    public  String messageId;

    @SerializedName("FromPeopleId")
    public String fromPeopleId;

    @SerializedName("ToPeopleIds")
    public String toPeopleIds;

    @SerializedName("ToPeopleNames")
    public String toPeopleNames;

    @SerializedName("ToPeopleEmails")
    public String toPeopleEmails;

    @SerializedName("Recipients")
    public String recipients;

    @SerializedName("BCC")
    public String bCC;

    @SerializedName("CC")
    public String cC;

    @SerializedName("BCCIds")
    public String bCCIds;

    @SerializedName("BCCNames")
    public String bCCNames;

    @SerializedName("BCCEmails")
    public String bCCEmails;

    @SerializedName("CCIds")
    public String cCIds;

    @SerializedName("CCNames")
    public String cCNames;

    @SerializedName("CCEmails")
    public String cCEmails;

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

    @SerializedName("Time")
    public String time;

    @SerializedName("NRFFlag")
    public String nRFFlag;

    @SerializedName("FromName")
    public String fromName;

    @SerializedName("dateday")
    public String Dateday;

    public BeanMessageDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageId);
        dest.writeString(this.fromPeopleId);
        dest.writeString(this.toPeopleIds);
        dest.writeString(this.toPeopleNames);
        dest.writeString(this.toPeopleEmails);
        dest.writeString(this.recipients);
        dest.writeString(this.bCC);
        dest.writeString(this.cC);
        dest.writeString(this.bCCIds);
        dest.writeString(this.bCCNames);
        dest.writeString(this.bCCEmails);
        dest.writeString(this.cCIds);
        dest.writeString(this.cCNames);
        dest.writeString(this.cCEmails);
        dest.writeString(this.subject);
        dest.writeString(this.body);
        dest.writeString(this.attachmentFlag);
        dest.writeString(this.parentId);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeString(this.nRFFlag);
        dest.writeString(this.fromName);
        dest.writeString(this.Dateday);
    }

    protected BeanMessageDetail(Parcel in) {
        this.messageId = in.readString();
        this.fromPeopleId = in.readString();
        this.toPeopleIds = in.readString();
        this.toPeopleNames = in.readString();
        this.toPeopleEmails = in.readString();
        this.recipients = in.readString();
        this.bCC = in.readString();
        this.cC = in.readString();
        this.bCCIds = in.readString();
        this.bCCNames = in.readString();
        this.bCCEmails = in.readString();
        this.cCIds = in.readString();
        this.cCNames = in.readString();
        this.cCEmails = in.readString();
        this.subject = in.readString();
        this.body = in.readString();
        this.attachmentFlag = in.readString();
        this.parentId = in.readString();
        this.date = in.readString();
        this.time = in.readString();
        this.nRFFlag = in.readString();
        this.fromName = in.readString();
        this.Dateday = in.readString();
    }

    public static final Creator<BeanMessageDetail> CREATOR = new Creator<BeanMessageDetail>() {
        @Override
        public BeanMessageDetail createFromParcel(Parcel source) {
            return new BeanMessageDetail(source);
        }

        @Override
        public BeanMessageDetail[] newArray(int size) {
            return new BeanMessageDetail[size];
        }
    };
}
