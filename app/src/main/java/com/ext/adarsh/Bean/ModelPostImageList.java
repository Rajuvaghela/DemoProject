package com.ext.adarsh.Bean;

import android.graphics.Bitmap;

/**
 * Created by ExT-Emp-011 on 1/16/2018.
 */

public class ModelPostImageList {

    Bitmap bitmap;
    String imagepath;
    String image_extension;
    String Image_name;
    String flag;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImage_extension() {
        return image_extension;
    }

    public void setImage_extension(String image_extension) {
        this.image_extension = image_extension;
    }

    public String getImage_name() {
        return Image_name;
    }

    public void setImage_name(String image_name) {
        Image_name = image_name;
    }


    public void setFlag(String flag){
        this.flag = flag;
    }


    public String getFlag() {
        return flag;
    }
}
