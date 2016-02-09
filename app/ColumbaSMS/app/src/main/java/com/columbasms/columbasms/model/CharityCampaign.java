package com.columbasms.columbasms.model;

import java.util.List;

/**
 * Created by matteobrienza on 1/30/16.
 */
public class CharityCampaign {
    /*
    {
        "id": 1,
        "message": "Prova campagna localizzata",
        "organization": {
                "id": 1,
                "organization_name": "Lorenzo Rapetti Onlus",
                "avatar_normal": "https://www.columbasms.com/system/organizations/avatars/000/000/001/normal/IMG_20160206_165924.jpg?1454778444"
    },
        "topics": [
            {
                "id": 1,
                "name": "Feeding the Hungry"
            }
        ]
    }
    */

    private String id;
    private String message;
    private Association organization;
    private List<Topic> topics;


    public CharityCampaign(String id, String message, Association organization, List<Topic> topics){
        this.id = id;
        this.message = message;
        this.organization = organization;
        this.topics = topics;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Association getOrganization() {
        return organization;
    }

    public List<Topic> getTopics() {
        return topics;
    }
}
