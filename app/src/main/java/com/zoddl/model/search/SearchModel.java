package com.zoddl.model.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class SearchModel {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("Taglist")
	private List<Taglist> taglist;

	@SerializedName("ResponseMessage")
	private String responseMessage;

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public void setTaglist(List<Taglist> taglist){
		this.taglist = taglist;
	}

	public List<Taglist> getTaglist(){
		return taglist;
	}

	public void setResponseMessage(String responseMessage){
		this.responseMessage = responseMessage;
	}

	public String getResponseMessage(){
		return responseMessage;
	}

}