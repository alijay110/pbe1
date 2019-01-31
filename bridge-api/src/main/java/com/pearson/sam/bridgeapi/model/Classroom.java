package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.CLASSROOM_ID_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_CREATEDON;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_DATE_CREATED;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_UPDATEDON;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.GROUP_TYPE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PROVIDE_VALID_NAME;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.VALIDATION_CODE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.YEAR_LEVEL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.elasticsearch.model.ClassroomSearch;
import com.pearson.sam.bridgeapi.validators.ValidateModelCollection;

import io.leangen.graphql.annotations.GraphQLIgnore;

@Document(collection = "ClassRooms")
public class Classroom extends BaseModel {
  /**
   * @param clsSrch
   */
 

  public Classroom(ClassroomSearch clsSrch) {
	
	this.id = clsSrch.getId();
	this.classroomId = clsSrch.getClassroomId();
	this.name = clsSrch.getName();
	this.dateCreated = clsSrch.getDateCreated();
	this.validationCode = clsSrch.getValidationCode();
	this.schoolId = clsSrch.getSchoolId();
	this.groupOwner = clsSrch.getGroupOwner();
	this.groupType = clsSrch.getGroupType();
	this.inactiveTimestamp = clsSrch.getInactiveTimestamp();
	this.orgIdentifier = clsSrch.getOrgIdentifier();
	this.productModelIdentifier = clsSrch.getProductModelIdentifier();
	this.updatedBy = clsSrch.getUpdatedBy();
	this.updatedOn = clsSrch.getUpdatedOn();
	this.activeTimestamp = clsSrch.getActiveTimestamp();
	this.createdBy = clsSrch.getCreatedBy();
	this.createdOn = clsSrch.getCreatedOn();
	this.owner = clsSrch.getOwner();
}

public Classroom() {}
	public Classroom(String id, String classroomId, String name, List<String> yearLevel,
      Subject subject, List<String> product, String dateCreated, String validationCode,
      String schoolId, List<Members> members, String groupOwner, String groupType,
      String inactiveTimestamp, String orgIdentifier, String productModelIdentifier,
      String updatedBy,
      @Pattern(regexp = "[0-9]*", message = ENTER_VALID_UPDATEDON) String updatedOn,
      String activeTimestamp, String createdBy,
      @Pattern(regexp = "[0-9]*", message = ENTER_VALID_CREATEDON) String createdOn, String owner,
      List<Product> productDetails, User ownerDetails) {
    super();
    this.id = id;
    this.classroomId = classroomId;
    this.name = name;
    this.yearLevel = yearLevel;
    this.subject = subject;
    this.product = product;
    this.dateCreated = dateCreated;
    this.validationCode = validationCode;
    this.schoolId = schoolId;
    this.members = members;
    this.groupOwner = groupOwner;
    this.groupType = groupType;
    this.inactiveTimestamp = inactiveTimestamp;
    this.orgIdentifier = orgIdentifier;
    this.productModelIdentifier = productModelIdentifier;
    this.updatedBy = updatedBy;
    this.updatedOn = updatedOn;
    this.activeTimestamp = activeTimestamp;
    this.createdBy = createdBy;
    this.createdOn = createdOn;
    this.owner = owner;
    this.productDetails = productDetails;
    this.ownerDetails = ownerDetails;
  }


  @Id
	private String id;
	/*
	 * to make it classroomId as unique in existing ClassRooms collection ,following
	 * command should be executed in mongodb of backend pp-dev and pp-QA
	 * db.ClassRooms.createIndex( { "classroomId": 1 }, { unique: true } )
	 * db.ClassRooms.createIndex( { "validationCode": 1 }, { unique: true } )
	 */
	@MongoSource
	@Pattern(regexp = "[0-9a-zA-z]*", message = CLASSROOM_ID_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private String classroomId;
	@MongoSource
	@Pattern(regexp = "[0-9a-zA-z+'_ ]*", message = PROVIDE_VALID_NAME)
	private String name;
	@MongoSource
	@ValidateModelCollection(message = YEAR_LEVEL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> yearLevel;
	@MongoSource
	private Subject subject;
	@MongoSource
	@ValidateModelCollection(message = PRODUCT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> product;
	@MongoSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_DATE_CREATED)
	private String dateCreated;
	@MongoSource
	@Pattern(regexp = "[0-9a-zA-z]*", message = VALIDATION_CODE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private String validationCode;

	@MongoSource
	@Pattern(regexp = "[0-9a-zA-z]*", message = SCHOOL_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private String schoolId;

	@MongoSource
	private List<Members> members;
	@SamSource
	private String groupOwner;
	@SamSource
	@Pattern(regexp = "[0-9a-zA-z]*", message = GROUP_TYPE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private String groupType;
	@SamSource
	private String inactiveTimestamp;
	@SamSource
	private String orgIdentifier;
	@SamSource
	private String productModelIdentifier;
	@MongoSource
	@SamSource
	private String updatedBy;
	@MongoSource
	@SamSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_UPDATEDON)
	private String updatedOn;
	@SamSource
	@GraphQLIgnore
	private String activeTimestamp;
	@MongoSource
	@SamSource
	private String createdBy;
	@MongoSource
	@SamSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_CREATEDON)
	private String createdOn;
	@MongoSource
	private String owner;
	@Valid
	private List<Product> productDetails;
	private User ownerDetails;

  @GraphQLIgnore
	public String getId() {
		return id;
	}

	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getYearLevel() {
		return yearLevel;
	}

	public void setYearLevel(List<String> yearLevel) {
		this.yearLevel = yearLevel;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public List<String> getProduct() {
		return product;
	}

	public void setProduct(List<String> product) {
		this.product = product;
	}

	public List<Members> getMembers() {
		return members;
	}

	public void setMembers(List<Members> members) {
		this.members = members;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public String getGroupOwner() {
		return groupOwner;
	}

	public void setGroupOwner(String groupOwner) {
		this.groupOwner = groupOwner;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getInactiveTimestamp() {
		return inactiveTimestamp;
	}

	public void setInactiveTimestamp(String inactiveTimestamp) {
		this.inactiveTimestamp = inactiveTimestamp;
	}

	public String getOrgIdentifier() {
		return orgIdentifier;
	}

	public void setOrgIdentifier(String orgIdentifier) {
		this.orgIdentifier = orgIdentifier;
	}

	public String getProductModelIdentifier() {
		return productModelIdentifier;
	}

	public void setProductModelIdentifier(String productModelIdentifier) {
		this.productModelIdentifier = productModelIdentifier;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getActiveTimestamp() {
		return activeTimestamp;
	}

	public void setActiveTimestamp(String activeTimestamp) {
		this.activeTimestamp = activeTimestamp;
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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<Product> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<Product> productDetails) {
		this.productDetails = productDetails;
	}

	public User getOwnerDetails() {
		return ownerDetails;
	}

	public void setOwnerDetails(User ownerDetails) {
		this.ownerDetails = ownerDetails;
	}

	@Override
	public String toString() {
		return "ClassRoom [id=" + id + ", classRoomId=" + classroomId + ", name=" + name + ", yearLevel=" + yearLevel
				+ ", subject=" + subject + ", product=" + product + ", members=" + members + ", dateCreated="
				+ dateCreated + "]";
	}

}
