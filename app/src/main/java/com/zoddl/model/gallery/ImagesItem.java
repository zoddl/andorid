package com.zoddl.model.gallery;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.zoddl.model.commommodel.SecondaryTag;
import com.google.gson.annotations.SerializedName;

public class ImagesItem implements Parcelable {

	@SerializedName("Prime_Name")
	private String primeName;

	@SerializedName("Description")
	private String description;

	@SerializedName("Primary_Tag")
	private String primaryTag;

	@SerializedName("Image_Url_Thumb")
	private String imageUrlThumb;

	@SerializedName("Tag_Send_Date")
	private String tagSendDate;

	@SerializedName("Amount")
	private String amount;

	@SerializedName("Sub_Description")
	private String subDescription;

	@SerializedName("Image_Url")
	private String imageUrl;

	@SerializedName("Tag_Status")
	private String tagStatus;

	@SerializedName("Customer_Id")
	private String customerId;

	@SerializedName("Doc_Url")
	private String docUrl;

	@SerializedName("isUploaded")
	private String isUploaded;

	@SerializedName("Tag_Type")
	private String tagType;

	@SerializedName("Id")
	private String id;

	@SerializedName("CGST")
	private String CGST;

	@SerializedName("SGST")
	private String SGST;

	@SerializedName("IGST")
	private String IGST;

	@SerializedName("Secondary_Tag")
	private List<SecondaryTag> secondaryTag;

	public ImagesItem() {
	}

	public ImagesItem(String mPrimeName, String mDescription, String mTagSendDate, String mAmount, String mTagStatus,String mTagType,String mIsUploaded,List<SecondaryTag> secTag,
			String mImageUrl,String mImageUrlThumb) {
		primeName = mPrimeName;
		description = mDescription;
		tagSendDate = mTagSendDate;
		amount = mAmount;
		tagStatus = mTagStatus;
		tagType = mTagType;
		isUploaded = mIsUploaded;
		secondaryTag = secTag;
		imageUrl = mImageUrl;
		imageUrlThumb = mImageUrlThumb;
	}

	public ImagesItem(String mImageUrl,String mImageUrlThumb, String mDocUrl, List<SecondaryTag> mSecondaryTag) {
		imageUrl = mImageUrl;
		imageUrlThumb = mImageUrlThumb;
		docUrl = mDocUrl;
		secondaryTag = mSecondaryTag;
	}

	protected ImagesItem(Parcel in) {
		primeName = in.readString();
		description = in.readString();
		primaryTag = in.readString();
		imageUrlThumb = in.readString();
		tagSendDate = in.readString();
		amount = in.readString();
		subDescription = in.readString();
		imageUrl = in.readString();
		tagStatus = in.readString();
		customerId = in.readString();
		docUrl = in.readString();
		isUploaded = in.readString();
		tagType = in.readString();
		id = in.readString();
		CGST = in.readString();
		SGST = in.readString();
		IGST = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(primeName);
		dest.writeString(description);
		dest.writeString(primaryTag);
		dest.writeString(imageUrlThumb);
		dest.writeString(tagSendDate);
		dest.writeString(amount);
		dest.writeString(subDescription);
		dest.writeString(imageUrl);
		dest.writeString(tagStatus);
		dest.writeString(customerId);
		dest.writeString(docUrl);
		dest.writeString(isUploaded);
		dest.writeString(tagType);
		dest.writeString(id);
		dest.writeString(CGST);
		dest.writeString(SGST);
		dest.writeString(IGST);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<ImagesItem> CREATOR = new Creator<ImagesItem>() {
		@Override
		public ImagesItem createFromParcel(Parcel in) {
			return new ImagesItem(in);
		}

		@Override
		public ImagesItem[] newArray(int size) {
			return new ImagesItem[size];
		}
	};

	public void setPrimeName(String primeName){
		this.primeName = primeName;
	}

	public String getPrimeName(){
		return primeName;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setPrimaryTag(String primaryTag){
		this.primaryTag = primaryTag;
	}

	public String getPrimaryTag(){
		return primaryTag;
	}

	public void setImageUrlThumb(String imageUrlThumb){
		this.imageUrlThumb = imageUrlThumb;
	}

	public String getImageUrlThumb(){
		return imageUrlThumb;
	}

	public void setTagSendDate(String tagSendDate){
		this.tagSendDate = tagSendDate;
	}

	public String getTagSendDate(){
		return tagSendDate;
	}

	public void setAmount(String amount){
		this.amount = amount;
	}

	public String getAmount(){
		return amount;
	}

	public void setSubDescription(String subDescription){
		this.subDescription = subDescription;
	}

	public String getSubDescription(){
		return subDescription;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setTagStatus(String tagStatus){
		this.tagStatus = tagStatus;
	}

	public String getTagStatus(){
		return tagStatus;
	}

	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}

	public String getCustomerId(){
		return customerId;
	}

	public void setDocUrl(String docUrl){
		this.docUrl = docUrl;
	}

	public String getDocUrl(){
		return docUrl;
	}

	public void setIsUploaded(String isUploaded){
		this.isUploaded = isUploaded;
	}

	public String getIsUploaded(){
		return isUploaded;
	}

	public void setTagType(String tagType){
		this.tagType = tagType;
	}

	public String getTagType(){
		return tagType;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public String getCGST() {
		return CGST;
	}

	public void setCGST(String mCGST) {
		CGST = mCGST;
	}

	public String getSGST() {
		return SGST;
	}

	public void setSGST(String mSGST) {
		SGST = mSGST;
	}

	public String getIGST() {
		return IGST;
	}

	public void setIGST(String mIGST) {
		IGST = mIGST;
	}

	public void setSecondaryTag(List<SecondaryTag> secondaryTag){
		this.secondaryTag = secondaryTag;
	}

	public List<SecondaryTag> getSecondaryTag(){
		return secondaryTag;
	}
}