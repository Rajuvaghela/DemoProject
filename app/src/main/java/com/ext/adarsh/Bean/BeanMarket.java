package com.ext.adarsh.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-001 on 04-08-2017.
 */

public class BeanMarket implements Parcelable {

    @SerializedName("AdvertisementId")
    public String advertisementId;

    @SerializedName("PeopleId")
    public String peopleId;

    @SerializedName("AdvertisementTitle")
    public String advertisementTitle;

    @SerializedName("AdvertisementType")
    public String advertisementType;

    @SerializedName("MarketCategoryId")
    public String marketCategoryId;

    @SerializedName("OfferPrice")
    public String offerPrice;

    @SerializedName("ImagePath1")
    public String imagePath1;

    @SerializedName("ImagePath2")
    public String imagePath2;

    @SerializedName("ImagePath3")
    public String imagePath3;

    @SerializedName("ImagePath4")
    public String imagePath4;

    @SerializedName("ImagePath5")
    public String imagePath5;

    @SerializedName("Description")
    public String description;

    @SerializedName("MarketCategoryName")
    public String marketCategoryName;

    @SerializedName("FullName")
    public String fullName;

    @SerializedName("RoleTitle")
    public String roleTitle;

    @SerializedName("AdvertisementTypeStatus")
    public String advertisementTypeStatus;

    @SerializedName("CityName")
    public String cityName;

    @SerializedName("CityId")
    public String cityId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.advertisementId);
        dest.writeString(this.peopleId);
        dest.writeString(this.advertisementTitle);
        dest.writeString(this.advertisementType);
        dest.writeString(this.marketCategoryId);
        dest.writeString(this.offerPrice);
        dest.writeString(this.imagePath1);
        dest.writeString(this.imagePath2);
        dest.writeString(this.imagePath3);
        dest.writeString(this.imagePath4);
        dest.writeString(this.imagePath5);
        dest.writeString(this.description);
        dest.writeString(this.marketCategoryName);
        dest.writeString(this.fullName);
        dest.writeString(this.roleTitle);
        dest.writeString(this.advertisementTypeStatus);
        dest.writeString(this.cityName);
        dest.writeString(this.cityId);
    }

    public BeanMarket() {
    }

    protected BeanMarket(Parcel in) {
        this.advertisementId = in.readString();
        this.peopleId = in.readString();
        this.advertisementTitle = in.readString();
        this.advertisementType = in.readString();
        this.marketCategoryId = in.readString();
        this.offerPrice = in.readString();
        this.imagePath1 = in.readString();
        this.imagePath2 = in.readString();
        this.imagePath3 = in.readString();
        this.imagePath4 = in.readString();
        this.imagePath5 = in.readString();
        this.description = in.readString();
        this.marketCategoryName = in.readString();
        this.fullName = in.readString();
        this.roleTitle = in.readString();
        this.advertisementTypeStatus = in.readString();
        this.cityName = in.readString();
        this.cityId = in.readString();
    }

    public static final Parcelable.Creator<BeanMarket> CREATOR = new Parcelable.Creator<BeanMarket>() {
        @Override
        public BeanMarket createFromParcel(Parcel source) {
            return new BeanMarket(source);
        }

        @Override
        public BeanMarket[] newArray(int size) {
            return new BeanMarket[size];
        }
    };
}