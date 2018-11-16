
package com.zoddl.model.home.HomeDetailTagWise;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Payload {

    @SerializedName("Count")
    private String mCount;
    @SerializedName("Images")
    private List<Image> mImages;
    @SerializedName("Primary_Tag")
    private String mPrimaryTag;
    @SerializedName("Prime_Name")
    private String mPrimeName;
    @SerializedName("Source_Type")
    private String mSourceType;
    @SerializedName("Total")
    private String mTotal;

    public String getCount() {
        return mCount;
    }

    public void setCount(String Count) {
        mCount = Count;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public void setImages(List<Image> Images) {
        mImages = Images;
    }

    public String getPrimaryTag() {
        return mPrimaryTag;
    }

    public void setPrimaryTag(String PrimaryTag) {
        mPrimaryTag = PrimaryTag;
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

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String Total) {
        mTotal = Total;
    }

}
