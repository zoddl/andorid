package com.zoddl.model.gallery;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class TaglistItem implements Parcelable {

	@SerializedName("Source_Type")private String sourceType;
	@SerializedName("Prime_Name")private String primeName;
	@SerializedName("Primary_Tag")private String primaryTag;
	@SerializedName("Total")private String total;
	@SerializedName("Images")private List<ImagesItem> images;
	@SerializedName("Count")private int count;
	@SerializedName("Tag_Date_int")private String tagDateInt;
	@SerializedName("Timestamp")private String timestamp;

	public TaglistItem(String mPrimaryTag, String mTotal,List<ImagesItem> image) {
		primeName = mPrimaryTag;
		total = mTotal;
		images = image;
	}

	public static Comparator<TaglistItem> SortWithNone = new Comparator<TaglistItem>() {

		public int compare(TaglistItem s1, TaglistItem s2) {

			String articleN1 = s1.getPrimeName().toLowerCase();
			String articleN2 = s2.getPrimeName().toLowerCase();

			return articleN1.compareTo(articleN2);

		}};

	public static Comparator<TaglistItem> SortWithAmount = new Comparator<TaglistItem>() {

		public int compare(TaglistItem s1, TaglistItem s2) {

			String articleN1 = s1.getTotal().toLowerCase();
			String articleN2 = s2.getTotal().toLowerCase();

			int diff = 0;
			try {
				diff = Integer.parseInt(articleN2) - Integer.parseInt(articleN1);
			} catch (NumberFormatException mE) {
				mE.printStackTrace();
			}
			return (diff == 0) ? articleN1.compareTo(articleN2) : diff;
		}};



	public static Comparator<TaglistItem> SortWithCount = new Comparator<TaglistItem>() {

		public int compare(TaglistItem s1, TaglistItem s2) {

			String articleN1 = String.valueOf(s1.getImages().size());
			String articleN2 = String.valueOf(s2.getImages().size());

			return articleN2.compareTo(articleN1);

		}};



	public static Comparator<TaglistItem> SortWithAlphabet = new Comparator<TaglistItem>() {

		public int compare(TaglistItem s1, TaglistItem s2) {

			String articleN1 = s1.getPrimeName().toLowerCase();
			String articleN2 = s2.getPrimeName().toLowerCase();

			return articleN1.compareTo(articleN2);

		}};


	protected TaglistItem(Parcel in) {
		sourceType = in.readString();
		primeName = in.readString();
		primaryTag = in.readString();
		total = in.readString();
		images = in.createTypedArrayList(ImagesItem.CREATOR);
		count = in.readInt();
		tagDateInt = in.readString();
		timestamp = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sourceType);
		dest.writeString(primeName);
		dest.writeString(primaryTag);
		dest.writeString(total);
		dest.writeTypedList(images);
		dest.writeInt(count);
		dest.writeString(tagDateInt);
		dest.writeString(timestamp);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<TaglistItem> CREATOR = new Creator<TaglistItem>() {
		@Override
		public TaglistItem createFromParcel(Parcel in) {
			return new TaglistItem(in);
		}

		@Override
		public TaglistItem[] newArray(int size) {
			return new TaglistItem[size];
		}
	};

	public void setSourceType(String sourceType){
		this.sourceType = sourceType;
	}

	public String getSourceType(){
		return sourceType;
	}

	public void setPrimeName(String primeName){
		this.primeName = primeName;
	}

	public String getPrimeName(){
		return primeName;
	}

	public void setPrimaryTag(String primaryTag){
		this.primaryTag = primaryTag;
	}

	public String getPrimaryTag(){
		return primaryTag;
	}

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
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

	public void setTagDateInt(String tagDateInt){
		this.tagDateInt = tagDateInt;
	}

	public String getTagDateInt(){
		return tagDateInt;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}
}