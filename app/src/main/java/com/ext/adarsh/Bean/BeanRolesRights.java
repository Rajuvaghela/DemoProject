package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 1/12/2018.
 */

public class BeanRolesRights implements Parcelable {

    @SerializedName("Id")
    public  String id;

    @SerializedName("RoleId")
    public  String roleId;

    @SerializedName("RightsId")
    public  String rightsId;

    @SerializedName("Title")
    public  String title;

    @SerializedName("R1")
    public  String r1;

    @SerializedName("R1Flag")
    public  String r1Flag;

    @SerializedName("R2")
    public  String r2;

    @SerializedName("R2Flag")
    public  String r2Flag;

    @SerializedName("R3")
    public  String r3;

    @SerializedName("R3Flag")
    public  String r3Flag;

    @SerializedName("R4")
    public  String r4;

    @SerializedName("R4Flag")
    public  String r4Flag;

    @SerializedName("R5")
    public  String r5;

    @SerializedName("R5Flag")
    public  String r5Flag;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.roleId);
        dest.writeString(this.rightsId);
        dest.writeString(this.title);
        dest.writeString(this.r1);
        dest.writeString(this.r1Flag);
        dest.writeString(this.r2);
        dest.writeString(this.r2Flag);
        dest.writeString(this.r3);
        dest.writeString(this.r3Flag);
        dest.writeString(this.r4);
        dest.writeString(this.r4Flag);
        dest.writeString(this.r5);
        dest.writeString(this.r5Flag);
    }

    public BeanRolesRights() {
    }

    protected BeanRolesRights(Parcel in) {
        this.id = in.readString();
        this.roleId = in.readString();
        this.rightsId = in.readString();
        this.title = in.readString();
        this.r1 = in.readString();
        this.r1Flag = in.readString();
        this.r2 = in.readString();
        this.r2Flag = in.readString();
        this.r3 = in.readString();
        this.r3Flag = in.readString();
        this.r4 = in.readString();
        this.r4Flag = in.readString();
        this.r5 = in.readString();
        this.r5Flag = in.readString();
    }

    public static final Parcelable.Creator<BeanRolesRights> CREATOR = new Parcelable.Creator<BeanRolesRights>() {
        @Override
        public BeanRolesRights createFromParcel(Parcel source) {
            return new BeanRolesRights(source);
        }

        @Override
        public BeanRolesRights[] newArray(int size) {
            return new BeanRolesRights[size];
        }
    };
}
