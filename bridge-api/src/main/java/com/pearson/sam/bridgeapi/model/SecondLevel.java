package com.pearson.sam.bridgeapi.model;

import java.util.List;

public class SecondLevel {
	
	private String title;
	private List<ResourceData> resourceData;
	private ThirdLevel thirdLevel;
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
	public ThirdLevel getThirdLevel() {
		return thirdLevel;
	}
	public void setThirdLevel(ThirdLevel thirdLevel) {
		this.thirdLevel = thirdLevel;
	}

	
}
