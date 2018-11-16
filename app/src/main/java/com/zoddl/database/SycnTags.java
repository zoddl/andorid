package com.zoddl.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by vipin on 3/5/18.
 */

@Entity
public class SycnTags {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "tid")
    private int tagId;

    @ColumnInfo(name = "primary_tag")
    private String primaryTag;

    @ColumnInfo(name = "secondary_tag")
    private String secondaryTag;

    @ColumnInfo(name = "tag_status")
    private String tagStatus;

    @ColumnInfo(name = "amount")
    private String amount;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "flow")
    private String flow;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "image_url_thumb")
    private String imageUrlThumb;

    @ColumnInfo(name = "document_url")
    private String documentUrl;

    @ColumnInfo(name = "auto_manual")
    private String autoManual;

    @ColumnInfo(name = "status")
    private String status;


    @ColumnInfo(name = "type")
    private String typeOfFile;

    public SycnTags() {
    }

    //for
    public SycnTags(String mPrimaryTag, String mSecondaryTag, String mTagStatus, String mAmount, String mDate, String mDescription, String mFlow, String mImageUrl, String mImageUrlThumb, String mDocumentUrl, String mAutoManual, String mStatus,String mType) {
        primaryTag = mPrimaryTag;
        secondaryTag = mSecondaryTag;
        tagStatus = mTagStatus;
        amount = mAmount;
        date = mDate;
        description = mDescription;
        flow = mFlow;
        imageUrl = mImageUrl;
        imageUrlThumb = mImageUrlThumb;
        documentUrl = mDocumentUrl;
        autoManual = mAutoManual;
        status = mStatus;
        typeOfFile = mType;
    }


    @NonNull
    public int getTagId() {
        return tagId;
    }

    public void setTagId(@NonNull int mTagId) {
        tagId = mTagId;
    }

    public String getPrimaryTag() {
        return primaryTag;
    }

    public void setPrimaryTag(String mPrimaryTag) {
        primaryTag = mPrimaryTag;
    }

    public String getSecondaryTag() {
        return secondaryTag;
    }

    public void setSecondaryTag(String mSecondaryTag) {
        secondaryTag = mSecondaryTag;
    }

    public String getTagStatus() {
        return tagStatus;
    }

    public void setTagStatus(String mTagStatus) {
        tagStatus = mTagStatus;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String mAmount) {
        amount = mAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String mDate) {
        date = mDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String mDescription) {
        description = mDescription;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String mFlow) {
        flow = mFlow;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        imageUrl = mImageUrl;
    }

    public String getImageUrlThumb() {
        return imageUrlThumb;
    }

    public void setImageUrlThumb(String mImageUrlThumb) {
        imageUrlThumb = mImageUrlThumb;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String mDocumentUrl) {
        documentUrl = mDocumentUrl;
    }

    public String getAutoManual() {
        return autoManual;
    }

    public void setAutoManual(String mAutoManual) {
        autoManual = mAutoManual;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String mStatus) {
        status = mStatus;
    }

    public String getTypeOfFile() {
        return typeOfFile;
    }

    public void setTypeOfFile(String mTypeOfFile) {
        typeOfFile = mTypeOfFile;
    }
}
