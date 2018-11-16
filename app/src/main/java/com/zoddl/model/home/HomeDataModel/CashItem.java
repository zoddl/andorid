package com.zoddl.model.home.HomeDataModel;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class CashItem{

	@SerializedName("Prime_Name")
	private String primeName;

	@SerializedName("Total_Amount")
	private String totalAmount;

	@SerializedName("Primary_Tag")
	private String primaryTag;

	@SerializedName("Tag_Type")
	private String tagType;

	@SerializedName("Total_Count")
	private String totalCount;

	public void setPrimeName(String primeName){
		this.primeName = primeName;
	}

	public String getPrimeName(){
		return primeName;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public void setPrimaryTag(String primaryTag){
		this.primaryTag = primaryTag;
	}

	public String getPrimaryTag(){
		return primaryTag;
	}

	public void setTagType(String tagType){
		this.tagType = tagType;
	}

	public String getTagType(){
		return tagType;
	}

	public void setTotalCount(String totalCount){
		this.totalCount = totalCount;
	}

	public String getTotalCount(){
		return totalCount;
	}
}