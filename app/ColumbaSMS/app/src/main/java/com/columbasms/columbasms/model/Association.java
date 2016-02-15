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
        private int followers;
        private boolean following;
        private boolean trusting;

        public Association (String id, String organization_name, String avatar_normal, String cover_normal, String description){
            this.id = id;
            this.organization_name = organization_name;
            this.avatar_normal = avatar_normal;
            this.cover_normal = cover_normal;
            this.description = description;
        }

        public Association (String id,String organization_name, String avatar_normal, String cover_normal, String description, int followers, boolean following, boolean trusting){
            this.id = id;
            this.organization_name = organization_name;
            this.avatar_normal = avatar_normal;
            this.cover_normal = cover_normal;
            this.description = description;
            this.followers = followers;
            this.following = following;
            this.trusting = trusting;
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

        public boolean isFollowing() {
            return following;
        }

        public int getFollowers() {
            return followers;
        }

        public boolean isTrusting() {
            return trusting;
        }
}
