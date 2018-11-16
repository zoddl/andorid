
package com.zoddl.model.homeReport;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Column1 {

    @SerializedName("Id")
    private String mId;
    @SerializedName("Prime_Name")
    private String mPrimeName;
    @SerializedName("Source_Type")
    private String mSourceType;
    @SerializedName("Tag_Type")
    private String mTagType;

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }

    public String getPrimeName() {
        return mPrimeName;
    }

    public void setPrimeName(String PrimeName) {
        mPrimeName = PrimeName;
    }

    public String getSourceType() {
        return mSourceType;
    }

    public void setSourceType(String SourceType) {
        mSourceType = SourceType;
    }

    public String getTagType() {
        return mTagType;
    }

    public void setTagType(String TagType) {
        mTagType = TagType;
    }

}
