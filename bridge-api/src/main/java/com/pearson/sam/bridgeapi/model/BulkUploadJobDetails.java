package com.pearson.sam.bridgeapi.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;

public class BulkUploadJobDetails extends BaseModel{
	
	@Id
	private String id;
	@MongoSource
	private Integer jobId;
	@MongoSource
	private Object[] csvData;
	@MongoSource
	private String[] csvHeaders;
	@MongoSource
	private String[] safeHeaders;
	@MongoSource
	private Object[] validCSVData;
	@MongoSource
	private Object[] invalidEmailData;
	@MongoSource
	private Object[] invalidPasswordData;
	@MongoSource
	private Object[] invalidYearLevelData;
	@MongoSource
	private Object[] invalidUserTypeData;
	@MongoSource
	private Object[] missingInformationData;
	@MongoSource
	private List<ExistingUserData> existingUserData;
	@MongoSource
	private String[] incorrectExistingUserHeaders;
	@MongoSource
	private String[] incorrectExistingUserData;
	@MongoSource
	private String[] incorrectExistingUserDescription;
	@MongoSource
	private String[] unchangedUserData;
	@MongoSource
	private String[] unchangedUserDescription;
	@MongoSource
	private String[] updatedUserData;
	@MongoSource
	private List<NewlyAddedUserData> newlyAddedUserData;
	@MongoSource
	private List<DeactivatedUserData> deactivatedUserData;
	@MongoSource
	private Integer errorCount;
	@MongoSource
	private Integer successCount;
	@MongoSource
	private String errorBoxMessage;
	@MongoSource
	private String successBoxMessage;
	
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
	public String[] getSafeHeaders() {
		return safeHeaders;
	}
	public void setSafeHeaders(String[] safeHeaders) {
		this.safeHeaders = safeHeaders;
	}
	public Object[] getValidCSVData() {
		return validCSVData;
	}
	public void setValidCSVData(Object[] validCSVData) {
		this.validCSVData = validCSVData;
	}
	public Object[] getInvalidEmailData() {
		return invalidEmailData;
	}
	public void setInvalidEmailData(Object[] invalidEmailData) {
		this.invalidEmailData = invalidEmailData;
	}
	public Object[] getInvalidPasswordData() {
		return invalidPasswordData;
	}
	public void setInvalidPasswordData(Object[] invalidPasswordData) {
		this.invalidPasswordData = invalidPasswordData;
	}
	public Object[] getInvalidYearLevelData() {
		return invalidYearLevelData;
	}
	public void setInvalidYearLevelData(Object[] invalidYearLevelData) {
		this.invalidYearLevelData = invalidYearLevelData;
	}
	public Object[] getInvalidUserTypeData() {
		return invalidUserTypeData;
	}
	public void setInvalidUserTypeData(Object[] invalidUserTypeData) {
		this.invalidUserTypeData = invalidUserTypeData;
	}
	public Object[] getMissingInformationData() {
		return missingInformationData;
	}
	public void setMissingInformationData(Object[] missingInformationData) {
		this.missingInformationData = missingInformationData;
	}
	public List<ExistingUserData> getExistingUserData() {
		return existingUserData;
	}
	public void setExistingUserData(List<ExistingUserData> existingUserData) {
		this.existingUserData = existingUserData;
	}
	public String[] getIncorrectExistingUserHeaders() {
		return incorrectExistingUserHeaders;
	}
	public void setIncorrectExistingUserHeaders(String[] incorrectExistingUserHeaders) {
		this.incorrectExistingUserHeaders = incorrectExistingUserHeaders;
	}
	public String[] getIncorrectExistingUserData() {
		return incorrectExistingUserData;
	}
	public void setIncorrectExistingUserData(String[] incorrectExistingUserData) {
		this.incorrectExistingUserData = incorrectExistingUserData;
	}
	public String[] getIncorrectExistingUserDescription() {
		return incorrectExistingUserDescription;
	}
	public void setIncorrectExistingUserDescription(String[] incorrectExistingUserDescription) {
		this.incorrectExistingUserDescription = incorrectExistingUserDescription;
	}
	public String[] getUnchangedUserData() {
		return unchangedUserData;
	}
	public void setUnchangedUserData(String[] unchangedUserData) {
		this.unchangedUserData = unchangedUserData;
	}
	public String[] getUnchangedUserDescription() {
		return unchangedUserDescription;
	}
	public void setUnchangedUserDescription(String[] unchangedUserDescription) {
		this.unchangedUserDescription = unchangedUserDescription;
	}
	public String[] getUpdatedUserData() {
		return updatedUserData;
	}
	public void setUpdatedUserData(String[] updatedUserData) {
		this.updatedUserData = updatedUserData;
	}
	public List<NewlyAddedUserData> getNewlyAddedUserData() {
		return newlyAddedUserData;
	}
	public void setNewlyAddedUserData(List<NewlyAddedUserData> newlyAddedUserData) {
		this.newlyAddedUserData = newlyAddedUserData;
	}
	public List<DeactivatedUserData> getDeactivatedUserData() {
		return deactivatedUserData;
	}
	public void setDeactivatedUserData(List<DeactivatedUserData> deactivatedUserData) {
		this.deactivatedUserData = deactivatedUserData;
	}
	public Integer getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}
	public Integer getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}
	public String getErrorBoxMessage() {
		return errorBoxMessage;
	}
	public void setErrorBoxMessage(String errorBoxMessage) {
		this.errorBoxMessage = errorBoxMessage;
	}
	public String getSuccessBoxMessage() {
		return successBoxMessage;
	}
	public void setSuccessBoxMessage(String successBoxMessage) {
		this.successBoxMessage = successBoxMessage;
	}
	
}
