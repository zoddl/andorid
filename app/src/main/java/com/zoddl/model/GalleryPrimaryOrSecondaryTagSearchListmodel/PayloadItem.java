package com.zoddl.model.GalleryPrimaryOrSecondaryTagSearchListmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PayloadItem implements Parcelable {

	@SerializedName("Prime_Name") private String primeName;
	@SerializedName("Secondary_Name")private String secondaryName;
	@SerializedName("Primary_Tag")private String primaryTag;
	@SerializedName("Secondary_Tag")private String secondaryTag;
	@SerializedName("Total")private int total;
	@SerializedName("Images")private List<ImagesItem> images;
	@SerializedName("Count")private int count;

	protected PayloadItem(Parcel in) {
		primeName = in.readString();
		secondaryName = in.readString();
		primaryTag = in.readString();
		total = in.readInt();
		images = in.createTypedArrayList(ImagesItem.CREATOR);
		count = in.readInt();
		secondaryTag = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(primeName);
		dest.writeString(secondaryName);
		dest.writeString(primaryTag);
		dest.writeInt(total);
		dest.writeTypedList(images);
		dest.writeInt(count);
		dest.writeString(secondaryTag);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<PayloadItem> CREATOR = new Creator<PayloadItem>() {
		@Override
		public PayloadItem createFromParcel(Parcel in) {
			return new PayloadItem(in);
		}

		@Override
		public PayloadItem[] newArray(int size) {
			return new PayloadItem[size];
		}
	};

	public void setPrimeName(String primeName){
		this.primeName = primeName;
	}

	public String getPrimeName(){
		return primeName;
	}

	public void setSecondaryName(String secondaryName){
		this.secondaryName = secondaryName;
	}

	public String getSecondaryName(){
		return secondaryName;
	}

	public void setPrimaryTag(String primaryTag){
		this.primaryTag = primaryTag;
	}

	public String getPrimaryTag(){
		return primaryTag;
	}

	public void setTotal(int total){
		this.total = total;
	}

	public int getTotal(){
		return total;
	}

	public void setImages(List<ImagesItem> images){
		this.images = images;
	}

	public List<ImagesItem> getImages(){
		return images;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setSecondaryTag(String secondaryTag){
		this.secondaryTag = secondaryTag;
	}

	public String getSecondaryTag(){
		return secondaryTag;
	}
}