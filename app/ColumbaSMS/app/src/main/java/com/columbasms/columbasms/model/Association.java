package com.columbasms.columbasms.model;

import java.util.List;

/**
 * Created by Matteo Brienza on 2/2/16.
 */
public class Association {

        private String name;
        private String description;
        private String thumbnail_image_url;
        private String cover_image_url;
        private int follower;
        private boolean isFavourite;

        public Association (String name, String description, String thumbnail_image_url, String cover_image_url,int follower, boolean isFavourite){
            this.name = name;
            this.description = description;
            this.thumbnail_image_url = thumbnail_image_url;
            this.cover_image_url = cover_image_url;
            this.follower = follower;
            this.isFavourite = isFavourite;
        }

        public String getName() {
            return name;
        }

    public String getDescription() {
        return description;
    }

    public String getThumbnail_image_url() {
        return thumbnail_image_url;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public int getFollower() {
            return follower;
        }

        public boolean isFavourite() {
                return isFavourite;
            }
}
