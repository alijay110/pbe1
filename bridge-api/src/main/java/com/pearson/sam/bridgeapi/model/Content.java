package com.pearson.sam.bridgeapi.model;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import java.util.List;

public class Content {
	
	@MongoSource
	private List<FirstLevel> firstLevel;

	public List<FirstLevel> getFirstLevel() {
		return firstLevel;
	}

	public void setFirstLevel(List<FirstLevel> firstLevel) {
		this.firstLevel = firstLevel;
	}

	
 
	
}
