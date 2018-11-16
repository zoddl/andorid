package com.zoddl.model.GalleryPrimaryOrSecondaryTagSearchListmodel;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class GalleryprimaryorsecondaryResponse{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("Payload")
	private List<PayloadItem> payload;

	@SerializedName("ResponseMessage")
	private String responseMessage;

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public void setPayload(List<PayloadItem> payload){
		this.payload = payload;
	}

	public List<PayloadItem> getPayload(){
		return payload;
	}

	public void setResponseMessage(String responseMessage){
		this.responseMessage = responseMessage;
	}

	public String getResponseMessage(){
		return responseMessage;
	}
}