package com.zoddl.fcmService;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Message {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "message_id")
    @SerializedName("message_id")
    @Expose
    private String messageId;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    @Expose
    private String title;

    @ColumnInfo(name = "alert")
    @SerializedName("alert")
    @Expose
    private String alert;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    @Expose
    private String type;


    @NonNull
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(@NonNull String mMessageId) {
        messageId = mMessageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String mAlert) {
        alert = mAlert;
    }

    public String getType() {
        return type;
    }

    public void setType(String mType) {
        type = mType;
    }
}