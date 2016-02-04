package com.columbasms.columbasms.model;

import java.util.List;

/**
 * Created by Matteo Brienza on 2/2/16.
 */
public class Association {

        private String name;
        private int follower;
        private boolean isFavourite;

        public Association (String name, int follower, boolean isFavourite){
            this.name = name;
            this.follower = follower;
            this.isFavourite = isFavourite;
        }

        public String getName() {
            return name;
        }

        public int getFollower() {
            return follower;
        }

        public boolean isFavourite() {
                return isFavourite;
            }
}
