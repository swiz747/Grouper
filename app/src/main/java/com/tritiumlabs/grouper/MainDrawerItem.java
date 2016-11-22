package com.tritiumlabs.grouper;

public class MainDrawerItem {

    String itemName;
    int icon;

    public MainDrawerItem(String itemName, int icon) {
        super();
        this.itemName = itemName;
        this.icon = icon;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        itemName = itemName;
    }

    public int getImgResID() {
        return icon;
    }

    public void setImgResID(int icon) {
        this.icon = icon;
    }
}