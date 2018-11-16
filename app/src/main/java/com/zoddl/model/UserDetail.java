package com.zoddl.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mobiloitte on 12/7/17.
 */

public class UserDetail implements Serializable {

    @SerializedName("id")
    private String id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;
    @SerializedName("phone")
    private String phone;
    @SerializedName("pan_number")
    private String panNumber;
    @SerializedName("aadhar_number")
    private String aadharNumber;
    @SerializedName("city")
    private String city;
    @SerializedName("company_name")
    private String companyName;
    @SerializedName("skype_id")
    private String skypeId;
    @SerializedName("gstn")
    private String gstn;
    @SerializedName("deviceType")
    private String deviceType;
    @SerializedName("deviceToken")
    private String deviceToken;
    @SerializedName("deviceId")
    private String deviceId;

    /**
     * @return id gets the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * To get the value of id
     *
     * @param id sets the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return firstName gets the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * To get the value of firstName
     *
     * @param firstName sets the firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return lastName gets the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * To get the value of lastName
     *
     * @param lastName sets the lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return email gets the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * To get the value of email
     *
     * @param email sets the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return image gets the value of image
     */
    public String getImage() {
        return image;
    }

    /**
     * To get the value of image
     *
     * @param image sets the image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return dob gets the value of dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * To get the value of dob
     *
     * @param dob sets the dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return gender gets the value of gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * To get the value of gender
     *
     * @param gender sets the gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return phone gets the value of phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * To get the value of phone
     *
     * @param phone sets the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return panNumber gets the value of panNumber
     */
    public String getPanNumber() {
        return panNumber;
    }

    /**
     * To get the value of panNumber
     *
     * @param panNumber sets the panNumber
     */
    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    /**
     * @return aadharNumber gets the value of aadharNumber
     */
    public String getAadharNumber() {
        return aadharNumber;
    }

    /**
     * To get the value of aadharNumber
     *
     * @param aadharNumber sets the aadharNumber
     */
    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    /**
     * @return city gets the value of city
     */
    public String getCity() {
        return city;
    }

    /**
     * To get the value of city
     *
     * @param city sets the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return companyName gets the value of companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * To get the value of companyName
     *
     * @param companyName sets the companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return skypeId gets the value of skypeId
     */
    public String getSkypeId() {
        return skypeId;
    }

    /**
     * To get the value of skypeId
     *
     * @param skypeId sets the skypeId
     */
    public void setSkypeId(String skypeId) {
        this.skypeId = skypeId;
    }

    /**
     * @return gstn gets the value of gstn
     */
    public String getGstn() {
        return gstn;
    }

    /**
     * To get the value of gstn
     *
     * @param gstn sets the gstn
     */
    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

    /**
     * @return deviceType gets the value of deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * To get the value of deviceType
     *
     * @param deviceType sets the deviceType
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return deviceToken gets the value of deviceToken
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * To get the value of deviceToken
     *
     * @param deviceToken sets the deviceToken
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * @return deviceId gets the value of deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * To get the value of deviceId
     *
     * @param deviceId sets the deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
