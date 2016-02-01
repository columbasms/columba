package com.columbasms.columbasms.model;

/**
 * Created by matteobrienza on 1/30/16.
 */
public class CharityCampaign {

    private String associationName;
    private String topic;
    private String message;
    //private String image_url;

    public CharityCampaign(String associationName,String topic, String message){
        this.associationName = associationName;
        this.topic = topic;
        this.message = message;
    }

    public String getAssociationName() {
        return associationName;
    }

    public String getTopic() {
        return topic;
    }

    public String getMessage() {
        return message;
    }
}
