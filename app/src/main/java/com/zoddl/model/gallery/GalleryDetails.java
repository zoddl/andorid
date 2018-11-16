package com.zoddl.model.gallery;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GalleryDetails {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("Taglist")
	private List<TaglistItem> taglist;

	@SerializedName("ResponseMessage")
	private String responseMessage;

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public void setTaglist(List<TaglistItem> taglist){
		this.taglist = taglist;
	}

	public List<TaglistItem> getTaglist(){
		return taglist;
	}

	public void setResponseMessage(String responseMessage){
		this.responseMessage = responseMessage;
	}

	public String getResponseMessage(){
		return responseMessage;
	}

}