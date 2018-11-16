package com.zoddl.model.home;

import java.util.List;
import javax.annotation.Generated;

import com.zoddl.model.commommodel.SecondaryTag;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class ImagesItem{

	@SerializedName("Prime_Name")
	private String primeName;

	@SerializedName("Description")
	private String description;

	@SerializedName("Primary_Tag")
	private String primaryTag;

	@SerializedName("Image_Url_Thumb")
	private String imageUrlThumb;

	@SerializedName("Tag_Send_Date")
	private String tagSendDate;

	@SerializedName("Amount")
	private String amount;

	@SerializedName("Sub_Description")
	private String subDescription;

	@SerializedName("Count")
	private String count;

	@SerializedName("Type")
	private String type;

	@SerializedName("Image_Url")
	private String imageUrl;

	@SerializedName("Tag_Status")
	private String tagStatus;

	@SerializedName("Customer_Id")
	private String customerId;

	@SerializedName("isUploaded")
	private String isUploaded;

	@SerializedName("Tag_Type")
	private String tagType;

	@SerializedName("Id")
	private String id;

	@SerializedName("Secondary_Tag")
	private List<SecondaryTag> secondaryTag;

	public void setPrimeName(String primeName){
		this.primeName = primeName;
	}

	public String getPrimeName(){
		return primeName;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setPrimaryTag(String primaryTag){
		this.primaryTag = primaryTag;
	}

	public String getPrimaryTag(){
		return primaryTag;
	}

	public void setImageUrlThumb(String imageUrlThumb){
		this.imageUrlThumb = imageUrlThumb;
	}

	public String getImageUrlThumb(){
		return imageUrlThumb;
	}

	public void setTagSendDate(String tagSendDate){
		this.tagSendDate = tagSendDate;
	}

	public String getTagSendDate(){
		return tagSendDate;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setSubDescription(String subDescription){
		this.subDescription = subDescription;
	}

	public String getSubDescription(){
		return subDescription;
	}

	public void setCount(String count){
		this.count = count;
	}

	public String getCount(){
		return count;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setTagStatus(String tagStatus){
		this.tagStatus = tagStatus;
	}

	public String getTagStatus(){
		return tagStatus;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public void setIsUploaded(String isUploaded){
		this.isUploaded = isUploaded;
	}

	public String getIsUploaded(){
		return isUploaded;
	}

	public void setTagType(String tagType){
		this.tagType = tagType;
	}

	public String getTagType(){
		return tagType;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setSecondaryTag(List<SecondaryTag> secondaryTag){
		this.secondaryTag = secondaryTag;
	}

	public List<SecondaryTag> getSecondaryTag(){
		return secondaryTag;
	}
}