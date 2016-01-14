package com.columbasms.columbasms.model;

/**
 * Created by Matteo Brienza on 1/14/16.
 */
public class Contact {

    private String contact_name;
    private String contact_number;
    private boolean isSelected;

    public Contact (String contact_name, String contact_number, boolean isSelected){
        this.contact_name = contact_name;
        this.contact_number = contact_name;
        this.isSelected = isSelected;
    }

    public String getContact_name(){
        return contact_name;
    }

    public String getContact_number(){
        return contact_number;
    }

    public boolean isSelected(){
        return isSelected;
    }

    public void setSelected(boolean b){
        isSelected = b;
    }
}
