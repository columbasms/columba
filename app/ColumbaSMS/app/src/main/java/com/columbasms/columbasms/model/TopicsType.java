package com.columbasms.columbasms.model;

/**
 * Created by Matteo Brienza on 1/14/16.
 */
public class TopicsType {

        private String type_name;
        private boolean isSelected;

        public TopicsType(String type_name, boolean isSelected){
            this.type_name = type_name;
            this.isSelected = isSelected;
        }

        public String getType_name(){
            return type_name;
        }

        public boolean isSelected(){
            return isSelected;
        }

        public void setSelected(boolean b){
            isSelected = b;
        }

        public String toString(){
            return "Type: " + type_name + " isSelected: " + ((isSelected)?"true":"false");
        }
}
