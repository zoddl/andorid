
package com.zoddl.model.report;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class ReportSecondaryTag {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "Id")
    @SerializedName("Id")
    private String mId;

    @ColumnInfo(name = "Source_Type")
    @SerializedName("Source_Type")
    private String mSourceType;

    @ColumnInfo(name = "Tag_Type")
    @SerializedName("Tag_Type")
    private String Tag_Type;

    @ColumnInfo(name = "Secondary_Name")
    @SerializedName("Secondary_Name")
    private String mSecondaryName;

    @ColumnInfo(name = "Secondary_Tag")
    @SerializedName("Secondary_Tag")
    private String mSecondaryTag;

    private boolean isSelected = false;

    public ReportSecondaryTag() {
    }

    public ReportSecondaryTag(@NonNull String mId, String mSourceType, String mTag_Type, String mSecondaryName, String mSecondaryTag) {
        this.mId = mId;
        this.mSourceType = mSourceType;
        Tag_Type = mTag_Type;
        this.mSecondaryName = mSecondaryName;
        this.mSecondaryTag = mSecondaryTag;
    }

    public String getSourceType() {
        return mSourceType;
    }

    public void setSourceType(String mSourceType) {
        this.mSourceType = mSourceType;
    }

    public String getTag_Type() {
        return Tag_Type;
    }

    public void setTag_Type(String mTag_Type) {
        Tag_Type = mTag_Type;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getSecondaryName() {
        return mSecondaryName;
    }

    public void setSecondaryName(String mSecondaryName) {
        this.mSecondaryName = mSecondaryName;
    }

    public String getSecondaryTag() {
        return mSecondaryTag;
    }

    public void setSecondaryTag(String mSecondaryTag) {
        this.mSecondaryTag = mSecondaryTag;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean mSelected) {
        isSelected = mSelected;
    }
}
