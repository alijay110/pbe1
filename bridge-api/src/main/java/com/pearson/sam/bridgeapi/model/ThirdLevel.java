package com.pearson.sam.bridgeapi.model;

import java.util.List;

public class ThirdLevel {
	
	private String title;
	private List<ResourceData> resourceData;
	private FourthLevel fourthLevel;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<ResourceData> getResourceData() {
		return resourceData;
	}
	public void setResourceData(List<ResourceData> resourceData) {
		this.resourceData = resourceData;
	}
	public FourthLevel getFourthLevel() {
		return fourthLevel;
	}
	public void setFourthLevel(FourthLevel fourthLevel) {
		this.fourthLevel = fourthLevel;
	}

}
