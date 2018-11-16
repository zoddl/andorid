
package com.zoddl.model.home.HomeDetailTagWise;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class HomeSeeAllModel {

    @SerializedName("Payload")
    private List<com.zoddl.model.home.HomeDetailTagWise.Payload> mPayload;
    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMessage")
    private String mResponseMessage;

    public List<com.zoddl.model.home.HomeDetailTagWise.Payload> getPayload() {
        return mPayload;
    }

    public void setPayload(List<com.zoddl.model.home.HomeDetailTagWise.Payload> Payload) {
        mPayload = Payload;
    }

    public String getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        mResponseCode = ResponseCode;
    }

    public String getResponseMessage() {
        return mResponseMessage;
    }

    public void setResponseMessage(String ResponseMessage) {
        mResponseMessage = ResponseMessage;
    }

}
