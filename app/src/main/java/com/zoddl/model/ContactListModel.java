package com.zoddl.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by garuv on 2/5/18.
 */

public class ContactListModel {

    @SerializedName("Message")private String Message;
    @SerializedName("Date")private String Date;

    public ContactListModel(String mMessage, String mDate) {
        Message = mMessage;
        Date = mDate;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String mMessage) {
        Message = mMessage;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String mDate) {
        Date = mDate;
    }
}
