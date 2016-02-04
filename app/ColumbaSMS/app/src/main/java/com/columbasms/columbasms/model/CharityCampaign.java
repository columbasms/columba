package com.columbasms.columbasms.model;

import android.graphics.Bitmap;

import com.squareup.picasso.RequestCreator;

/**
 * Created by matteobrienza on 1/30/16.
 */
public class CharityCampaign {

    private String associationName;
    private String topic;
    private String message;
    private String image_url;

    public CharityCampaign(String associationName,String topic, String message, String image_url){
        this.associationName = associationName;
        this.topic = topic;
        this.message = message;
        this.image_url = image_url;
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

    public String getImage_url() {
        return image_url;
    }

}
