package com.ext.adarsh.Bean;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ExT-Emp-011 on 11/27/2017.
 */

public class BeanFileArray implements Parcelable {


    @SerializedName("FolderName")
    public String folderName;

    @SerializedName("FileId")
    public String fileId;

    @SerializedName("FilePath")
    public String filePath;

    @SerializedName("MainFilePath")
    public String mainFilePath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.folderName);
        dest.writeString(this.fileId);
        dest.writeString(this.filePath);
        dest.writeString(this.mainFilePath);
    }

    public BeanFileArray() {
    }

    protected BeanFileArray(Parcel in) {
        this.folderName = in.readString();
        this.fileId = in.readString();
        this.filePath = in.readString();
        this.mainFilePath = in.readString();
    }

    public static final Parcelable.Creator<BeanFileArray> CREATOR = new Parcelable.Creator<BeanFileArray>() {
        @Override
        public BeanFileArray createFromParcel(Parcel source) {
            return new BeanFileArray(source);
        }

        @Override
        public BeanFileArray[] newArray(int size) {
            return new BeanFileArray[size];
        }
    };
}
