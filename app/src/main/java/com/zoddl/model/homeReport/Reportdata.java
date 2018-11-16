
package com.zoddl.model.homeReport;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Reportdata {

    @SerializedName("Images")
    private List<ImageReport> mImages;
    @SerializedName("Tag_Type")
    private String mTagType;

    public List<ImageReport> getImages() {
        return mImages;
    }

    public void setImages(List<ImageReport> Images) {
        mImages = Images;
    }

    public String getTagType() {
        return mTagType;
    }

    public void setTagType(String TagType) {
        mTagType = TagType;
    }

}
