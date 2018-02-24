package com.ext.adarsh.Bean;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
public class BeanMarketCategory implements Parcelable {


    @SerializedName("MarketCategoryId")
    public  String marketCategoryId;

    @SerializedName("MarketCategoryName")
    public  String marketCategoryName;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.marketCategoryId);
        dest.writeString(this.marketCategoryName);
    }

    public BeanMarketCategory() {
    }
    public BeanMarketCategory(String marketCategoryId,String marketCategoryName ){
        this.marketCategoryName=marketCategoryName;
        this.marketCategoryId=marketCategoryId;

    }

    protected BeanMarketCategory(Parcel in) {
        this.marketCategoryId = in.readString();
        this.marketCategoryName = in.readString();
    }

    public static final Parcelable.Creator<BeanMarketCategory> CREATOR = new Parcelable.Creator<BeanMarketCategory>() {
        @Override
        public BeanMarketCategory createFromParcel(Parcel source) {
            return new BeanMarketCategory(source);
        }

        @Override
        public BeanMarketCategory[] newArray(int size) {
            return new BeanMarketCategory[size];
        }
    };
}
