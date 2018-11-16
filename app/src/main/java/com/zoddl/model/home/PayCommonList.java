package com.zoddl.model.home;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by garuv on 16/5/18.
 */

public class PayCommonList {
    @SerializedName("Total")
    private int total;

    @SerializedName("Images")
    private List<ImagesItem> images;

    @SerializedName("Tag_Type")
    private String tagType;

    public PayCommonList(String tagType, int total, List<ImagesItem> images) {
        this.tagType = tagType;
        this.total = total;
        this.images = images;
    }

    public void setTotal(int total){
        this.total = total;
    }

    public int getTotal(){
        return total;
    }

    public void setImages(List<ImagesItem> images){
        this.images = images;
    }

    public List<ImagesItem> getImages(){
        return images;
    }

    public void setTagType(String tagType){
        this.tagType = tagType;
    }

    public String getTagType(){
        return tagType;
    }
}
