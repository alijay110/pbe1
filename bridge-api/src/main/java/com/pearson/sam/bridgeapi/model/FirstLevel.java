package com.pearson.sam.bridgeapi.model;

import java.util.List;

public class FirstLevel {
	
	private String title;
	private List<ResourceData> resourceData;
	private List<SecondLevel> secondLevel;
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
	public List<SecondLevel> getSecondLevel() {
		return secondLevel;
	}
	public void setSecondLevel(List<SecondLevel> secondLevel) {
		this.secondLevel = secondLevel;
	}
	

}
