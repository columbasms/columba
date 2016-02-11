package com.columbasms.columbasms.model;

/**
 * Created by Matteo Brienza on 1/14/16.
 */
public class Topic {

        private String id;
        private String name;
        private boolean isSelected;
        private String mainColor;
        private String statusColor;

        public Topic(String id, String name, boolean isSelected,String mainColor,String statusColor){
            this.id = id;
            this.name = name;
            this.isSelected = isSelected;
            this.mainColor = mainColor;
            this.statusColor = statusColor;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }


        public boolean isSelected(){
            return isSelected;
        }


        public String getMainColor() {
            return mainColor;
        }

        public String getStatusColor() {
            return statusColor;
        }


        public void setSelected(boolean b){
            isSelected = b;
        }




}
