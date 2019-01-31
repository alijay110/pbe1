package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_QUANTITY_MIN;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_TOTAL_REACTIVATIONS_MAX;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ACCESS_CODE_TOTAL_REACTIVATIONS_MIN;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_DATE_CREATED;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_END_DATE;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_LAST_ACTIVATED_DATE;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_START_DATE;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_SUBSCRIPTION_DATE;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PROVIDE_VALID_USERNAME;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.types.GraphQLType;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accesscode")
@GraphQLType(name = "AccessCode")
public class AccessCodes extends BaseModel {
	@Id
	private String id;

	@SamSource
	@MongoSource
	private String accessCode;

	@MongoSource
	private String code;

	@SamSource
	@MongoSource
	private List<String> products;

	@MongoSource
	@SamSource
	@Pattern(regexp="^[0-9]*$",message=ENTER_VALID_START_DATE)
	private String startDate;

	@MongoSource
	@SamSource
	@Pattern(regexp="^[0-9]*$",message=ENTER_VALID_END_DATE)
	private String endDate;

	@MongoSource
	private String type;

	@MongoSource
	private Boolean isVoid;
	
	@Max(value=3,message=ACCESS_CODE_TOTAL_REACTIVATIONS_MAX+"{value}")
	@Min(value=0,message=ACCESS_CODE_TOTAL_REACTIVATIONS_MIN+"{value}")
	@MongoSource
	private Integer totalReactivations;

	@MongoSource
	private String batch;

	@SamSource
	@Min(value=1,message=ACCESS_CODE_QUANTITY_MIN)
	private Integer quantity;

	@MongoSource
	@SamSource
	private String createdBy;

	@MongoSource
	@SamSource
	@Pattern(regexp="[0-9]*$",message=ENTER_VALID_DATE_CREATED)
	private String dateCreated;

	@MongoSource
	@SamSource
	private String lastActivatedBy;

	@MongoSource
	@SamSource
	@Pattern(regexp="^[0-9]*",message=ENTER_VALID_LAST_ACTIVATED_DATE)
	private String lastActivatedDate;

	@SamSource
	@Pattern(regexp="^[0-9]*$",message=ENTER_VALID_SUBSCRIPTION_DATE)
	private String subscriptionDate;

	@SamSource
	private String subscriptionIdentifier;

	@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9@._-]*$", message = PROVIDE_VALID_USERNAME)
	@SamSource
	private String userName;

	
  /**
   * @param accessCodeSrch
   */
  public AccessCodes(AccessCodeSearch accessCodeSrch) {
    this.id=accessCodeSrch.getId();
    this.accessCode=accessCodeSrch.getAccessCode();
    this.code=accessCodeSrch.getCode();
    this.products=accessCodeSrch.getProducts();
	this.startDate=accessCodeSrch.getStartDate();
	this.endDate=accessCodeSrch.getEndDate();
	this.type=accessCodeSrch.getType();
	this.isVoid=accessCodeSrch.getIsVoid();
	this.totalReactivations=accessCodeSrch.getTotalReactivations();
	this.batch=accessCodeSrch.getBatch();
	this.quantity=accessCodeSrch.getQuantity();
	this.createdBy=accessCodeSrch.getCreatedBy();
	this.dateCreated=accessCodeSrch.getDateCreated();
	this.lastActivatedBy=accessCodeSrch.getLastActivatedBy();
	this.lastActivatedDate=accessCodeSrch.getLastActivatedDate();
	this.subscriptionDate=accessCodeSrch.getSubscriptionDate();
	this.subscriptionIdentifier=accessCodeSrch.getSubscriptionIdentifier();
	this.userName=accessCodeSrch.getUserName();
	
  }
	/**
   * 
   */
  public AccessCodes() {
    // TODO Auto-generated constructor stub
  }


  public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	@GraphQLIgnore
	public String getId() {
		return id;
	}

	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}

	public Boolean getIsVoid() {
		return isVoid;
	}

	public void setIsVoid(Boolean isVoid) {
		this.isVoid = isVoid;
	}

	public Integer getTotalReactivations() {
		return totalReactivations;
	}

	public void setTotalReactivations(Integer totalReactivations) {
		this.totalReactivations = totalReactivations;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the subscriptionDate
	 */
	public String getSubscriptionDate() {
		return subscriptionDate;
	}

	/**
	 * @param subscriptionDate the subscriptionDate to set
	 */
	public void setSubscriptionDate(String subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	/**
	 * @return the subscriptionIdentifier
	 */
	public String getSubscriptionIdentifier() {
		return subscriptionIdentifier;
	}

	/**
	 * @param subscriptionIdentifier the subscriptionIdentifier to set
	 */
	public void setSubscriptionIdentifier(String subscriptionIdentifier) {
		this.subscriptionIdentifier = subscriptionIdentifier;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
