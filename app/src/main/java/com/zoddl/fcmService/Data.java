package com.zoddl.fcmService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("message")
    @Expose
    private List<Message> message = null;

    /**
     * @return Gets the value of message and returns message
     */
    public List<Message> getMessage() {
        return message;
    }

    /**
     * Sets the message
     * You can use getMessage() to get the value of message
     */
    public void setMessage(List<Message> message) {
        this.message = message;
    }
}