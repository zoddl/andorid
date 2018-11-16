
package com.zoddl.model.home.HomeDetailTagWise;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Image {

    @SerializedName("Amount")
    private String mAmount;
    @SerializedName("Customer_Id")
    private String mCustomerId;
    @SerializedName("Description")
    private String mDescription;
    @SerializedName("Id")
    private String mId;
    @SerializedName("Image_Url")
    private String mImageUrl;
    @SerializedName("Image_Url_Thumb")
    private String mImageUrlThumb;
    @SerializedName("isUploaded")
    private String mIsUploaded;
    @SerializedName("Primary_Tag")
    private String mPrimaryTag;
    @SerializedName("Prime_Name")
    private String mPrimeName;
    @SerializedName("Secondary_Tag")
    private List<com.zoddl.model.home.HomeDetailTagWise.SecondaryTag> mSecondaryTag;
    @SerializedName("Sub_Description")
    private String mSubDescription;
    @SerializedName("Tag_Send_Date")
    private String mTagSendDate;
    @SerializedName("Tag_Status")
    private String mTagStatus;
    @SerializedName("Tag_Type")
    private String mTagType;
    @SerializedName("Type")
    private String mType;

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String Amount) {
        mAmount = Amount;
    }

    public String getCustomerId() {
        return mCustomerId;
    }

    public void setCustomerId(String CustomerId) {
        mCustomerId = CustomerId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String Description) {
        mDescription = Description;
    }

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        mImageUrl = ImageUrl;
    }

    public String getImageUrlThumb() {
        return mImageUrlThumb;
    }

    public void setImageUrlThumb(String ImageUrlThumb) {
        mImageUrlThumb = ImageUrlThumb;
    }

    public String getIsUploaded() {
        return mIsUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        mIsUploaded = isUploaded;
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

    public List<com.zoddl.model.home.HomeDetailTagWise.SecondaryTag> getSecondaryTag() {
        return mSecondaryTag;
    }

    public void setSecondaryTag(List<com.zoddl.model.home.HomeDetailTagWise.SecondaryTag> SecondaryTag) {
        mSecondaryTag = SecondaryTag;
    }

    public String getSubDescription() {
        return mSubDescription;
    }

    public void setSubDescription(String SubDescription) {
        mSubDescription = SubDescription;
    }

    public String getTagSendDate() {
        return mTagSendDate;
    }

    public void setTagSendDate(String TagSendDate) {
        mTagSendDate = TagSendDate;
    }

    public String getTagStatus() {
        return mTagStatus;
    }

    public void setTagStatus(String TagStatus) {
        mTagStatus = TagStatus;
    }

    public String getTagType() {
        return mTagType;
    }

    public void setTagType(String TagType) {
        mTagType = TagType;
    }

    public String getType() {
        return mType;
    }

    public void setType(String Type) {
        mType = Type;
    }

}
