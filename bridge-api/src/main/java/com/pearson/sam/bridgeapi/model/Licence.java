package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_CREATION_DATE;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_DATE_CREATED;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_LAST_ACTIVATED_DATE;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;

import io.leangen.graphql.annotations.GraphQLIgnore;

@Document(collection = "licence")
public class Licence extends BaseModel{
  @Id
  private String id;

  @MongoSource
  String licenceId;

  @MongoSource
  String attachedSchool;

  @MongoSource
  String attachedOrganisation;

  @MongoSource
  String attachedUser;


  @MongoSource
  String type;


  @MongoSource
  String activationDate;

  @MongoSource
  String expirationDate;

  @MongoSource
  @Pattern(regexp = "[0-9]*", message = ENTER_VALID_CREATION_DATE)
  String creationDate;
  
  @SamSource
  List<String> products;
  
  @MongoSource
  @SamSource
  private String createdBy;
	
  @SamSource
  @MongoSource
  @Pattern(regexp = "[0-9]*", message = ENTER_VALID_DATE_CREATED)
  private String dateCreated;
	
  @SamSource
  @MongoSource
  private String lastActivatedBy;
	
  @SamSource
  @MongoSource
  @Pattern(regexp = "[0-9]*", message = ENTER_VALID_LAST_ACTIVATED_DATE)
  private String lastActivatedDate;

  public String getLicenceId() {
    return licenceId;
  }

  public void setLicenceId(String licenceId) {
    this.licenceId = licenceId;
  }

  public String getAttachedSchool() {
    return attachedSchool;
  }

  public void setAttachedSchool(String attachedSchool) {
    this.attachedSchool = attachedSchool;
  }

  public String getAttachedOrganisation() {
    return attachedOrganisation;
  }

  public void setAttachedOrganisation(String attachedOrganisation) {
    this.attachedOrganisation = attachedOrganisation;
  }

  public String getAttachedUser() {
    return attachedUser;
  }

  public void setAttachedUser(String attachedUser) {
    this.attachedUser = attachedUser;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  

  public String getActivationDate() {
    return activationDate;
  }

  public void setActivationDate(String activationDate) {
    this.activationDate = activationDate;
  }

  public String getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(String expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public List<String> getProducts() {
    return products;
  }

  public void setProducts(List<String> products) {
    this.products = products;
  }
    
    @GraphQLIgnore
	public String getId() {
		return id;
	}
	
    @GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	public String getLastActivatedBy() {
		return lastActivatedBy;
	}
	
	public void setLastActivatedBy(String lastActivatedBy) {
		this.lastActivatedBy = lastActivatedBy;
	}
	
	public String getLastActivatedDate() {
		return lastActivatedDate;
	}
	
	public void setLastActivatedDate(String lastActivatedDate) {
		this.lastActivatedDate = lastActivatedDate;
	}

}
