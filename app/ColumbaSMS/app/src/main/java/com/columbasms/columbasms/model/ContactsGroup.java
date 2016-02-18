package com.columbasms.columbasms.model;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Matteo Brienza on 2/16/16.
 */
public class ContactsGroup {

    private String name;
    private JSONArray contactList;
    private boolean isSelected;

    public ContactsGroup(String name, JSONArray contactList, boolean isSelected){
        this.name = name;
        this.contactList = contactList;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public JSONArray getContactList() {
        return contactList;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
