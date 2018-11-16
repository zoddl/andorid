package com.zoddl.model.home.HomeDataModel;

import java.util.List;


public class PayloadItem{
	private String columnHeading;

	public String getColumnHeading() {
		return columnHeading;
	}

	public void setColumnHeading(String columnHeading) {
		this.columnHeading = columnHeading;
	}

	private List<CashItem> cash;
	public void setCash(List<CashItem> cash){
		this.cash = cash;
	}
	public List<CashItem> getCash(){
		return cash;
	}
}
