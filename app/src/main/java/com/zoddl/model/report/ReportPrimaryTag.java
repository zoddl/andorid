package com.zoddl.model.report;

import com.google.gson.annotations.SerializedName;

/**
 * Created by garuv on 2/5/18.
 */

public class ReportPrimaryTag{

    @SerializedName("Source_Type")private String mSourceType;
    @SerializedName("Tag_Type")private String mTagType;
    @SerializedName("Id")private String Id;
    @SerializedName("Prime_Name")private String mPrimeName;
    @SerializedName("Primary_Tag")private String mPrimaryTag;
    private boolean isSelected = false;

    public ReportPrimaryTag(String mItem) {
        this.mPrimeName = mItem;
    }

    public String getSourceType() {
        return mSourceType;
    }

    public void setSourceType(String mSourceType) {
        this.mSourceType = mSourceType;
    }

    public String getTagType() {
        return mTagType;
    }

    public void setTagType(String mTagType) {
        this.mTagType = mTagType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String mId) {
        Id = mId;
    }

    public String getPrimeName() {
        return mPrimeName;
    }

    public void setPrimeName(String mPrimeName) {
        this.mPrimeName = mPrimeName;
    }

    public String getPrimaryTag() {
        return mPrimaryTag;
    }

    public void setPrimaryTag(String mPrimaryTag) {
        this.mPrimaryTag = mPrimaryTag;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean mSelected) {
        isSelected = mSelected;
    }
}
