package com.pearson.sam.bridgeapi.model;

public class ChapterResources {

	private String resourceId;
	private String title;
	private Information information;
	private String type;
	private Boolean teacherResource;
	private String createdOn;
	private String createdBy;
	private String resourceFile;
	private String updatedOn;
	private String updatedBy;
	
	
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Information getInformation() {
		return information;
	}
	public void setInformation(Information information) {
		this.information = information;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getTeacherResource() {
		return teacherResource;
	}
	public void setTeacherResource(Boolean teacherResource) {
		this.teacherResource = teacherResource;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getResourceFile() {
		return resourceFile;
	}
	public void setResourceFile(String resourceFile) {
		this.resourceFile = resourceFile;
	}
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}