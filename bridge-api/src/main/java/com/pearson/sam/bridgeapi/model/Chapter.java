package com.pearson.sam.bridgeapi.model;

import java.util.List;

public class Chapter {

	private String name;
	private List<ChapterResources> chapterResource;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ChapterResources> getChapterResource() {
		return chapterResource;
	}
	public void setChapterResource(List<ChapterResources> chapterResource) {
		this.chapterResource = chapterResource;
	}
	
	
}