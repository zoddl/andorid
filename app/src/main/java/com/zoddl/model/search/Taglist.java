
package com.zoddl.model.search;

import java.util.List;
import javax.annotation.Generated;

import com.zoddl.model.postdetials.Image;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Taglist {

    @SerializedName("Images")
    private List<Image> mImages;
    @SerializedName("Primary_Tag")
    private String mPrimaryTag;
    @SerializedName("Prime_Name")
    private String mPrimeName;
    @SerializedName("Total")
    private String mTotal;
    @SerializedName("Uses")
    private String mUses;

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

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String Total) {
        mTotal = Total;
    }

    public String getUses() {
        return mUses;
    }

    public void setUses(String Uses) {
        mUses = Uses;
    }

}
