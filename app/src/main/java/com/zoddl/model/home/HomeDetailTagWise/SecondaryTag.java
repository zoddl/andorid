
package com.zoddl.model.home.HomeDetailTagWise;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class SecondaryTag {

    @SerializedName("Id")
    private String mId;
    @SerializedName("Secondary_Name")
    private String mSecondaryName;

    public String getId() {
        return mId;
    }

    public void setId(String Id) {
        mId = Id;
    }

    public String getSecondaryName() {
        return mSecondaryName;
    }

    public void setSecondaryName(String SecondaryName) {
        mSecondaryName = SecondaryName;
    }

}
