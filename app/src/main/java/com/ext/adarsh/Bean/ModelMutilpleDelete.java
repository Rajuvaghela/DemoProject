package com.ext.adarsh.Bean;

/**
 * Created by ExT-Emp-001 on 10-02-2018.
 */

public class ModelMutilpleDelete{

    public int position;
    public String id;

    public ModelMutilpleDelete(String id,int position){
        this.id = id;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
