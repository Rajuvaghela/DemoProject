package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 01-11-2017.
 */

public class BeanDepartmentList implements Parcelable {

    private boolean isSelected = false;
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    @SerializedName("DepartmentId")
    public String departmentId;

    @SerializedName("DepartmentName")
    public String departmentName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.departmentId);
        dest.writeString(this.departmentName);
    }

    public BeanDepartmentList() {
    }

    public BeanDepartmentList(String departmentId, String departmentName) {
        this.departmentName = departmentName;
        this.departmentId = departmentId;

    }

    protected BeanDepartmentList(Parcel in) {
        this.departmentId = in.readString();
        this.departmentName = in.readString();
    }

    public static final Creator<BeanDepartmentList> CREATOR = new Creator<BeanDepartmentList>() {
        @Override
        public BeanDepartmentList createFromParcel(Parcel source) {
            return new BeanDepartmentList(source);
        }

        @Override
        public BeanDepartmentList[] newArray(int size) {
            return new BeanDepartmentList[size];
        }
    };
}
