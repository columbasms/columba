package com.columbasms.columbasms.model;

import java.util.List;

/**
 * Created by Matteo Brienza on 2/2/16.
 */
public class Association {

        private String id;
        private String organization_name;
        private String avatar_normal;
        private String cover_normal;
        private String description;
        //private String topic;
        //private int follower;
        //private boolean isFavourite;

        public Association (String id, String organization_name, String avatar_normal, String cover_normal, String description){
            this.id = id;
            this.organization_name = organization_name;
            this.avatar_normal = avatar_normal;
            this.cover_normal = cover_normal;
            this.description = description;

            //this.topic = topic;
            //this.follower = follower;
            //this.isFavourite = isFavourite;
        }


        public String getId() {
            return id;
        }

        public String getOrganization_name() {
            return organization_name;
        }

        public String getAvatar_normal() {
            return avatar_normal;
        }

        public String getCover_normal() {
            return cover_normal;
        }

        public String getDescription() {
            return description;
        }


        /*
        public String getTopic() {
            return topic;
        }

        public int getFollower() {
                return follower;
            }

        public boolean isFavourite() {
                    return isFavourite;
                }
         */
}
