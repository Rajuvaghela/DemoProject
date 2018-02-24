package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 12/9/2017.
 */

public class BeanPeopleSelectByDepartmentId implements Parcelable {



    private boolean isSelected = false;
    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

    @SerializedName("PeopleId")
    public  String peopleId;

    @SerializedName("PeopleName")
    public  String peopleName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.peopleId);
        dest.writeString(this.peopleName);
    }

    public BeanPeopleSelectByDepartmentId(String peopleId,String peopleName ) {
        this.peopleId=peopleId;
        this.peopleName=peopleName;
    }
    public BeanPeopleSelectByDepartmentId() {
    }

    protected BeanPeopleSelectByDepartmentId(Parcel in) {
        this.peopleId = in.readString();
        this.peopleName = in.readString();
    }

    public static final Parcelable.Creator<BeanPeopleSelectByDepartmentId> CREATOR = new Parcelable.Creator<BeanPeopleSelectByDepartmentId>() {
        @Override
        public BeanPeopleSelectByDepartmentId createFromParcel(Parcel source) {
            return new BeanPeopleSelectByDepartmentId(source);
        }

        @Override
        public BeanPeopleSelectByDepartmentId[] newArray(int size) {
            return new BeanPeopleSelectByDepartmentId[size];
        }
    };
}
