package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */

public class BeanBranchList implements Parcelable {

    private boolean isSelected = false;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    @SerializedName("BranchId")
    public String branchId;

    @SerializedName("BranchName")
    public String branchName;

    public BeanBranchList() {

    }

    public BeanBranchList(String branchId, String branchName) {
        this.branchName = branchName;
        this.branchId = branchId;

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.branchId);
        dest.writeString(this.branchName);
    }

    protected BeanBranchList(Parcel in) {
        this.branchId = in.readString();
        this.branchName = in.readString();
    }

    public static final Creator<BeanBranchList> CREATOR = new Creator<BeanBranchList>() {
        @Override
        public BeanBranchList createFromParcel(Parcel source) {
            return new BeanBranchList(source);
        }

        @Override
        public BeanBranchList[] newArray(int size) {
            return new BeanBranchList[size];
        }
    };
}
