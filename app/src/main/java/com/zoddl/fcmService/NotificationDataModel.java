package com.zoddl.fcmService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationDataModel {

    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * @return Gets the value of data and returns data
     */
    public Data getData() {
        return data;
    }

    /**
     * Sets the data
     * You can use getData() to get the value of data
     */
    public void setData(Data data) {
        this.data = data;
    }
}