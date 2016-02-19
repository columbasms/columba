package com.columbasms.columbasms.model;

/**
 * Created by Matteo Brienza on 1/14/16.
 */
public class Topic {

        private String id;
        private String name;
        private boolean followed;
        private String mainColor;
        private String statusColor;
        private String image;


        public Topic(String id, String name, boolean followed,String mainColor,String statusColor,String image){
            this.id = id;
            this.name = name;
            this.followed = followed;
            this.mainColor = mainColor;
            this.statusColor = statusColor;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }


    public boolean isFollowed() {
        return followed;
    }

    public String getMainColor() {
            return mainColor;
        }

    public String getStatusColor() {
            return statusColor;
        }


    public void setFollowed(boolean b){
            followed = b;
        }

    public String getImage() {
        return image;
    }
}
