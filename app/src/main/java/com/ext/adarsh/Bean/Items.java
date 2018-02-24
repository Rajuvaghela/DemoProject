package com.ext.adarsh.Bean;

/**
 * Created by ExT-Emp-011 on 8/3/2017.
 */

public class Items {
    private String itemName;
    private int image;



    public Items(String itemName, int image) {
        this.itemName = itemName;
        this.image = image;


    }

    public String getItemName() {
        return itemName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
