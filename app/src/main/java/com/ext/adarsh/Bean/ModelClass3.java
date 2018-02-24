package com.ext.adarsh.Bean;

/**
 * Created by ExT-Emp-011 on 11/16/2017.
 */

public class ModelClass3 {

    String priority;

    String id;

    String name;
    String email_id;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public ModelClass3() {
    }

    public ModelClass3(String priority, String id, String name) {
        this.priority = priority;
        this.id = id;
        this.name = name;
    }

    private boolean isSelected = false;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
