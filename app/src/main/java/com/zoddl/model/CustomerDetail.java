package com.zoddl.model;

/**
 * Created by admin1 on 5/4/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerDetail {

    @SerializedName("Customerid")
    @Expose
    private String customerid;
    @SerializedName("Paid_Status")
    @Expose
    private String paidStatus;
    @SerializedName("Authtoken")
    @Expose
    private String authtoken;
    @SerializedName("BaseUrl")
    @Expose
    private String baseUrl;

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}