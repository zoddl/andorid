package com.zoddl.model.home;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

@Entity
public class HomePrimaryTagPayload {

	@PrimaryKey
	@NonNull
	@ColumnInfo(name = "Id")
	@SerializedName("Id")
	String Id;

	@ColumnInfo(name = "Source_Type")
	@SerializedName("Source_Type")
	private String Source_Type;

	@ColumnInfo(name = "Tag_Type")
	@SerializedName("Tag_Type")
	private String Tag_Type;

	@ColumnInfo(name = "Prime_Name")
	@SerializedName("Prime_Name")
	private String Prime_Name;

	@ColumnInfo(name = "Primary_Tag")
	@SerializedName("Primary_Tag")
	private String Primary_Tag;

	public HomePrimaryTagPayload() {
	}

	public HomePrimaryTagPayload(@NonNull String mId, String mSource_Type, String mTag_Type, String mPrime_Name, String mPrimary_Tag) {
		Id = mId;
		Source_Type = mSource_Type;
		Tag_Type = mTag_Type;
		Prime_Name = mPrime_Name;
		Primary_Tag = mPrimary_Tag;
	}

	public String getSource_Type() {
		return Source_Type;
	}

	public void setSource_Type(String mSource_Type) {
		Source_Type = mSource_Type;
	}

	public String getTag_Type() {
		return Tag_Type;
	}

	public void setTag_Type(String mTag_Type) {
		Tag_Type = mTag_Type;
	}

	public String getId() {
		return Id;
	}

	public void setId(String mId) {
		Id = mId;
	}

	public String getPrime_Name() {
		return Prime_Name;
	}

	public void setPrime_Name(String mPrime_Name) {
		Prime_Name = mPrime_Name;
	}

	public String getPrimary_Tag() {
		return Primary_Tag;
	}

	public void setPrimary_Tag(String mPrimary_Tag) {
		Primary_Tag = mPrimary_Tag;
	}
}