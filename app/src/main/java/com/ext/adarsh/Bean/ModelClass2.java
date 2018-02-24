package com.ext.adarsh.Bean;

/**
 * Created by ExT-Emp-001 on 02-11-2017.
 */

public class ModelClass2 {
    String index_no;
    String email_id;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getIndex_no() {
        return index_no;
    }

    public void setIndex_no(String index_no) {
        this.index_no = index_no;
    }

    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String text;
    private boolean isSelected = false;

    public String getText() {
        return text;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

}
