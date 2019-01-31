package com.pearson.sam.bridgeapi.model;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;

import io.leangen.graphql.annotations.GraphQLIgnore;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Schools")
public class School extends BaseModel {
  @Id
  private String id;
  @SamSource
  private String schoolId;

  @SamSource
  @MongoSource
  private String name;
  @MongoSource
  private Location location;
  @MongoSource
  private String team;
  @MongoSource
  private List<String> tags;
  @SamSource
  @MongoSource
  private String schoolType;
  @MongoSource
  private String schoolCode;
  @MongoSource
  @SamSource
  private String createdBy;
  @MongoSource
  @SamSource
  @Pattern(regexp = "[0-9]*", message = "Enter Valid createdOn")
  private String createdOn;
  @MongoSource
  @SamSource
  private String updatedBy;
  @MongoSource
  @SamSource
  @Pattern(regexp = "[0-9]*", message = "Enter Valid updatedOn")
  private String updatedOn;
  @SamSource
  private String productModelIdentifier;
  @SamSource
  private String schoolURL;
  @MongoSource
  private String schoolStatus;
  @MongoSource
  private List<String> product;
  @MongoSource
  private String teacherCode;
  @MongoSource
  private String studentCode;
  @MongoSource
  private String educationStage;
  @MongoSource
  private String organisation;

  private String activeTimestamp;

  private Long usersCount;
  private Long classRoomsCount;

  @GraphQLIgnore
  public String getId() {
    return id;
  }

  @GraphQLIgnore
  public void setId(String id) {
    this.id = id;
  }

  public String getSchoolId() {
    return schoolId;
  }

  public void setSchoolId(String schoolId) {
    this.schoolId = schoolId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getTeam() {
    return team;
  }

  public void setTeam(String team) {
    this.team = team;
  }

  public String getSchoolType() {
    return schoolType;
  }

  public void setSchoolType(String schoolType) {
    this.schoolType = schoolType;
  }

  public String getSchoolCode() {
    return schoolCode;
  }

  public void setSchoolCode(String schoolCode) {
    this.schoolCode = schoolCode;
  }

  public List<String> getProduct() {
    return product;
  }

  public void setProduct(List<String> product) {
    this.product = product;
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

  public String getProductModelIdentifier() {
    return productModelIdentifier;
  }

  public void setProductModelIdentifier(String productModelIdentifier) {
    this.productModelIdentifier = productModelIdentifier;
  }

  public String getSchoolURL() {
    return schoolURL;
  }

  public String getSchoolStatus() {
    return schoolStatus;
  }

  public void setSchoolStatus(String schoolStatus) {
    this.schoolStatus = schoolStatus;
  }

  public void setSchoolURL(String schoolURL) {
    this.schoolURL = schoolURL;
  }

  public String getTeacherCode() {
    return teacherCode;
  }

  public void setTeacherCode(String teacherCode) {
    this.teacherCode = teacherCode;
  }

  public String getStudentCode() {
    return studentCode;
  }

  public void setStudentCode(String studentCode) {
    this.studentCode = studentCode;
  }

  public @GraphQLIgnore String getActiveTimestamp() {
    return activeTimestamp;
  }

  @GraphQLIgnore
  public void setActiveTimestamp(String activeTimestamp) {
    this.activeTimestamp = activeTimestamp;
  }

  /**
   * @return the educationStage
   */
  public String getEducationStage() {
    return educationStage;
  }

  /**
   * @param educationStage
   *          the educationStage to set
   */
  public void setEducationStage(String educationStage) {
    this.educationStage = educationStage;
  }

  public String getOrganisation() {
    return organisation;
  }

  public void setOrganisation(String organisation) {
    this.organisation = organisation;
  }

  public Long getUsersCount() {
    return usersCount;
  }

  public void setUsersCount(Long usersCount) {
    this.usersCount = usersCount;
  }

  public Long getClassRoomsCount() {
    return classRoomsCount;
  }

  public void setClassRoomsCount(Long classRoomsCount) {
    this.classRoomsCount = classRoomsCount;
  }

  public School(String id, String schoolId, String name, Location location, String team,
      List<String> tags, String schoolType, String schoolCode, String createdBy, String createdOn,
      String updatedBy, String updatedOn, String productModelIdentifier, String schoolURL,
      String schoolStatus, List<String> product, String teacherCode, String studentCode,
      String educationStage, String organisation, String activeTimestamp, Long usersCount,
      Long classRoomsCount) {
    super();
    this.id = id;
    this.schoolId = schoolId;
    this.name = name;
    this.location = location;
    this.team = team;
    this.tags = tags;
    this.schoolType = schoolType;
    this.schoolCode = schoolCode;
    this.createdBy = createdBy;
    this.createdOn = createdOn;
    this.updatedBy = updatedBy;
    this.updatedOn = updatedOn;
    this.productModelIdentifier = productModelIdentifier;
    this.schoolURL = schoolURL;
    this.schoolStatus = schoolStatus;
    this.product = product;
    this.teacherCode = teacherCode;
    this.studentCode = studentCode;
    this.educationStage = educationStage;
    this.organisation = organisation;
    this.activeTimestamp = activeTimestamp;
    this.usersCount = usersCount;
    this.classRoomsCount = classRoomsCount;
  }

  public School(SchoolSearch schoolSrch) {
    super();
    this.id = schoolSrch.getId();
    this.schoolId = schoolSrch.getSchoolId();
    this.name = schoolSrch.getName();
    this.location = schoolSrch.getLocation();
    this.organisation = schoolSrch.getOrganisation();
    this.teacherCode = schoolSrch.getTeacherCode();
    this.studentCode = schoolSrch.getStudentCode();
    this.team = schoolSrch.getTeam();
    this.tags = schoolSrch.getTags();
    this.schoolType = schoolSrch.getSchoolType();
    this.schoolCode = schoolSrch.getSchoolCode();
    this.createdBy = schoolSrch.getCreatedBy();
    this.createdOn = schoolSrch.getCreatedOn();
    this.updatedBy = schoolSrch.getUpdatedBy();
    this.updatedOn = schoolSrch.getUpdatedOn();
    this.productModelIdentifier = schoolSrch.getProductModelIdentifier();
    this.schoolURL = schoolSrch.getSchoolURL();
    this.schoolStatus = schoolSrch.getSchoolStatus();
    this.product = schoolSrch.getProduct();
    this.educationStage = schoolSrch.getEducationStage();
    this.activeTimestamp = schoolSrch.getActiveTimestamp();
    this.usersCount = schoolSrch.getUsersCount();
    this.classRoomsCount = schoolSrch.getClassRoomsCount();

  }

  public void replaceWith(School school) {

    this.id = school.getId();
    this.schoolId = school.getSchoolId();
    this.name = school.getName();
    this.location = school.getLocation();
    this.team = school.getTeam();
    this.schoolType = school.getSchoolType();
    this.schoolCode = school.getSchoolCode();
    this.createdBy = school.getCreatedBy();
    this.createdOn = school.getCreatedOn();
    this.updatedBy = school.getUpdatedBy();
    this.updatedOn = school.getUpdatedOn();
    this.productModelIdentifier = school.getProductModelIdentifier();
    this.schoolURL = school.getSchoolURL();
    this.schoolStatus = school.getSchoolStatus();
    this.product = school.getProduct();
    this.teacherCode = school.getTeacherCode();
    this.studentCode = school.getStudentCode();
    this.educationStage = school.getEducationStage();
    this.organisation = school.getOrganisation();
    this.activeTimestamp = school.getActiveTimestamp();
    this.usersCount = school.getUsersCount();
    this.classRoomsCount = school.getClassRoomsCount();

  }

  public School() {
    super();
  }

}
