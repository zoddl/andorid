package com.zoddl.eventBusModels;

/**
 * @author abhishek.tiwari on 9/2/17
 * for Mobiloitte Technologies (I) Pvt. Ltd.
 */
public class DeviceTokenEvent {
    private String deviceToken;

    public DeviceTokenEvent(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * @return Gets the value of deviceToken and returns deviceToken
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * Sets the deviceToken
     * You can use getDeviceToken() to get the value of deviceToken
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
