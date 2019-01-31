package com.pearson.sam.bridgeapi.model;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

public class ResourceData {
	
	@MongoSource
	private String resourceTitle;
	@MongoSource
	private String description;
	
	@MongoSource
	private String createdOn;
	
	@MongoSource
	private String createdBy;
	
	@MongoSource
	private String resourceUrl;
	
	@MongoSource
	private String  roleAccess;
	
	@MongoSource
	private String fileType;
	
	@MongoSource
	private String sectionOrder;
	
	@MongoSource
	private String order;
	
	@MongoSource
	private Boolean isDownload;
	
	@MongoSource
	private String updatedOn;
	
	@MongoSource
	private String updateBy;

	public String getResourceTitle() {
		return resourceTitle;
	}

	public void setResourceTitle(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public String getRoleAccess() {
		return roleAccess;
	}

	public void setRoleAccess(String roleAccess) {
		this.roleAccess = roleAccess;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	
	public String getSectionOrder() {
		return sectionOrder;
	}

	public void setSectionOrder(String sectionOrder) {
		this.sectionOrder = sectionOrder;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Boolean getIsDownload() {
		return isDownload;
	}

	public void setIsDownload(Boolean isDownload) {
		this.isDownload = isDownload;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	
	

}
