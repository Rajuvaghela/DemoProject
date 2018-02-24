package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 05-11-2017.
 */

public class BeanRoleList implements Parcelable {

    @SerializedName("RoleId")
    public  String  roleId;

    @SerializedName("RoleTitle")
    public  String roleTitle;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.roleId);
        dest.writeString(this.roleTitle);
    }

    public BeanRoleList() {
    }
    public BeanRoleList(String roleId, String roleTitle) {
        this.roleTitle = roleTitle;
        this.roleId = roleId;

    }

    protected BeanRoleList(Parcel in) {
        this.roleId = in.readString();
        this.roleTitle = in.readString();
    }

    public static final Creator<BeanRoleList> CREATOR = new Creator<BeanRoleList>() {
        @Override
        public BeanRoleList createFromParcel(Parcel source) {
            return new BeanRoleList(source);
        }

        @Override
        public BeanRoleList[] newArray(int size) {
            return new BeanRoleList[size];
        }
    };
}
