package com.pearson.sam.bridgeapi.model;

import org.springframework.data.annotation.Id;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;

public class BulkUploadJobSummary extends BaseModel{
	
	@Id
	private String id;
	@MongoSource
	private Integer jobId;
	@MongoSource
	private String uploadType;
	@MongoSource
	private String operation;
	@MongoSource
	private String status;
	@MongoSource
	private String fileName;
	@MongoSource
	private String selectedUserRole;
	@MongoSource
	private String schoolId;
	@MongoSource
	private String organisationId;
	@MongoSource
	private Object[] validCSVData;
	@MongoSource
	private Object[] csvData;
	@MongoSource
	private String[] csvHeaders;
	@MongoSource
	private String requesterId;
	@MongoSource
	private String dateCreated;
	@MongoSource
	private String dateModified;
	
	@GraphQLIgnore
	public String getId() {
		return id;
	}
	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}
	public Integer getJobId() {
		return jobId;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	public String getUploadType() {
		return uploadType;
	}
	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSelectedUserRole() {
		return selectedUserRole;
	}
	public void setSelectedUserRole(String selectedUserRole) {
		this.selectedUserRole = selectedUserRole;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}
	public Object[] getValidCSVData() {
		return validCSVData;
	}
	public void setValidCSVData(Object[] validCSVData) {
		this.validCSVData = validCSVData;
	}
	public Object[] getCsvData() {
		return csvData;
	}
	public void setCsvData(Object[] csvData) {
		this.csvData = csvData;
	}
	public String[] getCsvHeaders() {
		return csvHeaders;
	}
	public void setCsvHeaders(String[] csvHeaders) {
		this.csvHeaders = csvHeaders;
	}
	public String getRequesterId() {
		return requesterId;
	}
	public void setRequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	
}
