package com.zoddl.navigationdrawer;

/**
 * Created by Abhishek Tiwari on 3/7/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class DrawerItem {
    private boolean showNotify;
    private String title;

    public DrawerItem() {
    }

    public DrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    /**
     * @return Gets the value of showNotify and returns showNotify
     */
    public boolean isShowNotify() {
        return showNotify;
    }

    /**
     * Sets the showNotify
     * You can use getShowNotify() to get the value of showNotify
     */
    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    /**
     * @return Gets the value of title and returns title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * You can use getTitle() to get the value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
