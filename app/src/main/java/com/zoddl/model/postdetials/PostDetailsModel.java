
package com.zoddl.model.postdetials;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PostDetailsModel {

    @SerializedName("Flag")
    private Long mFlag;
    @SerializedName("Payload")
    private List<com.zoddl.model.postdetials.Payload> mPayload;
    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMessage")
    private String mResponseMessage;

    public Long getFlag() {
        return mFlag;
    }

    public void setFlag(Long Flag) {
        mFlag = Flag;
    }

    public List<com.zoddl.model.postdetials.Payload> getPayload() {
        return mPayload;
    }

    public void setPayload(List<com.zoddl.model.postdetials.Payload> Payload) {
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
