package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_CREATEDON;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_UPDATEDON;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ORGANISATION_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PLEASE_PROVIDE_VALID_NAME;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.validators.ValidateModelCollection;

import io.leangen.graphql.annotations.GraphQLIgnore;

@Document(collection = "organisation")
public class Organisation extends BaseModel {

	@Id
	private String id;

	@SamSource
	@MongoSource
	@Pattern(regexp = "[0-9a-zA-z]*", message = ORGANISATION_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private String organisationId;

	@SamSource
	@Pattern(regexp = "^[a-zA-Z+' -]*$", message = PLEASE_PROVIDE_VALID_NAME)
	private String name;

	@MongoSource
	private String location;

	@MongoSource
	@ValidateModelCollection(message = SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> schools;
	@MongoSource
	@SamSource
	private String createdBy;
	@MongoSource
	@SamSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_CREATEDON)
	private String createdOn;
	@MongoSource
	@SamSource
	private String updatedBy;
	@MongoSource
	@SamSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_UPDATEDON)
	private String updatedOn;

	@SamSource
	private String productModelIdentifier;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<String> getSchools() {
		return schools;
	}

	public void setSchools(List<String> schools) {
		this.schools = schools;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getProductModelIdentifier() {
		return productModelIdentifier;
	}

	public void setProductModelIdentifier(String productModelIdentifier) {
		this.productModelIdentifier = productModelIdentifier;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	@GraphQLIgnore
	public String getId() {
		return id;
	}

	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}

	public String getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
