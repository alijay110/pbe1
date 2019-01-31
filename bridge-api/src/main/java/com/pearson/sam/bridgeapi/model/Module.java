package com.pearson.sam.bridgeapi.model;

import java.util.List;

public class Module {
	private String heading;
	private String type;
	//private List<ModuleResources> moduleResources;
	private List<Chapter> chapter;
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public List<Chapter> getChapter() {
		return chapter;
	}

	public void setChapter(List<Chapter> chapter) {
		this.chapter = chapter;
	}

		
	
}