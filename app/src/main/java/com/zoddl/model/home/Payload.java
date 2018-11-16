package com.zoddl.model.home;

import javax.annotation.Generated;

import com.zoddl.model.homeReport.Reportdata;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Payload{

	@SerializedName("Cashminusdata")
	private HomeCommonData cashminusdata;

	@SerializedName("Documentdata")
	private HomeCommonData documentdata;

	@SerializedName("Cashplusdata")
	private HomeCommonData cashplusdata;

	@SerializedName("Otherdata")
	private HomeCommonData otherdata;

	@SerializedName("Gallerydata")
	private HomeCommonData gallerydata;

	@SerializedName("Reportdata")
	private Reportdata reportdata;

	@SerializedName("Bankminusdata")
	private HomeCommonData bankminusdata;

	@SerializedName("Bankplusdata")
	private HomeCommonData bankplusdata;

	public Payload(HomeCommonData cashminusdata, HomeCommonData documentdata, HomeCommonData cashplusdata, HomeCommonData otherdata, HomeCommonData gallerydata, Reportdata reportdata, HomeCommonData bankminusdata, HomeCommonData bankplusdata) {
		this.cashminusdata = cashminusdata;
		this.documentdata = documentdata;
		this.cashplusdata = cashplusdata;
		this.otherdata = otherdata;
		this.gallerydata = gallerydata;
		this.reportdata = reportdata;
		this.bankminusdata = bankminusdata;
		this.bankplusdata = bankplusdata;
	}

	public HomeCommonData getCashminusdata() {
		return cashminusdata;
	}

	public void setCashminusdata(HomeCommonData mCashminusdata) {
		cashminusdata = mCashminusdata;
	}

	public HomeCommonData getDocumentdata() {
		return documentdata;
	}

	public void setDocumentdata(HomeCommonData mDocumentdata) {
		documentdata = mDocumentdata;
	}

	public HomeCommonData getCashplusdata() {
		return cashplusdata;
	}

	public void setCashplusdata(HomeCommonData mCashplusdata) {
		cashplusdata = mCashplusdata;
	}

	public HomeCommonData getOtherdata() {
		return otherdata;
	}

	public void setOtherdata(HomeCommonData mOtherdata) {
		otherdata = mOtherdata;
	}

	public HomeCommonData getGallerydata() {
		return gallerydata;
	}

	public void setGallerydata(HomeCommonData mGallerydata) {
		gallerydata = mGallerydata;
	}

	public Reportdata getReportdata() {
		return reportdata;
	}

	public void setReportdata(Reportdata mReportdata) {
		reportdata = mReportdata;
	}

	public HomeCommonData getBankminusdata() {
		return bankminusdata;
	}

	public void setBankminusdata(HomeCommonData mBankminusdata) {
		bankminusdata = mBankminusdata;
	}

	public HomeCommonData getBankplusdata() {
		return bankplusdata;
	}

	public void setBankplusdata(HomeCommonData mBankplusdata) {
		bankplusdata = mBankplusdata;
	}
}