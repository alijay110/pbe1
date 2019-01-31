package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_SHOULD_NOT_EMPTY;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.types.GraphQLType;

@Document(collection = "accesscodehistory")
@GraphQLType(name = "AccessCodeHistory")
public class AccessCodesHistory extends BaseModel {
	@Id
	private String id;
	
	@MongoSource
	private String accessCodeHistoryId;
	
	@NotBlank(message = ACCESS_CODE_SHOULD_NOT_EMPTY)
	@NotNull(message = ACCESS_CODE_SHOULD_NOT_EMPTY)
	@MongoSource
	private String accessCode;
	
	@MongoSource
	private AccessCodes alteration;
	
	@Pattern(regexp="[0-9]*",message="Enter Valid Alter Date")
	@MongoSource
	private String alterDate;
	
	@MongoSource
	private String alteredBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccessCodeHistoryId() {
		return accessCodeHistoryId;
	}

	public void setAccessCodeHistoryId(String accessCodeHistoryId) {
		this.accessCodeHistoryId = accessCodeHistoryId;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public AccessCodes getAlteration() {
		return alteration;
	}

	public void setAlteration(AccessCodes alteration) {
		this.alteration = alteration;
	}

	public String getAlterDate() {
		return alterDate;
	}

	public void setAlterDate(String alterDate) {
		this.alterDate = alterDate;
	}

	public String getAlteredBy() {
		return alteredBy;
	}

	public void setAlteredBy(String alteredBy) {
		this.alteredBy = alteredBy;
	}
	
	

}
