package com.pearson.sam.bridgeapi.model;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch;

import io.leangen.graphql.annotations.GraphQLIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "voucher")
public class Voucher extends BaseModel {

  @Id
  private String id;

  @MongoSource
  private String voucherId;

  @MongoSource
  @Pattern(regexp = "[0-9]*", message = "Enter Valid createdDate")
  private String createdDate;

  @MongoSource
  private String voucherCode;
  @MongoSource
  @Pattern(regexp = "^[0-9]*", message = "Enter Valid startDate")
  private String startDate;
  @MongoSource
  @Pattern(regexp = "^[0-9]*", message = "Enter Valid endDate")
  private String endDate;
  @MongoSource
  private String type;
  @MongoSource
  private Boolean isVoid;
  @MongoSource
  private String accessCode;
  @MongoSource
  @Max(value = 4, message = "totalReactivations must be less than {value}")
  @Min(value = 1, message = "totalReactivations Cannot be less than {value}")
  private Long totalReactivations;

  /**
   * @param voucherCodeSearch
   */
  public Voucher(VoucherCodeSearch voucherCodeSearch) {
    this.id = voucherCodeSearch.getId();
    this.voucherCode = voucherCodeSearch.getVoucherCode();
    this.voucherId = voucherCodeSearch.getVoucherId();
    this.createdDate = voucherCodeSearch.getCreatedDate();
    this.startDate = voucherCodeSearch.getStartDate();
    this.endDate = voucherCodeSearch.getEndDate();
    this.type = voucherCodeSearch.getType();
    this.isVoid = voucherCodeSearch.getIsVoid();
    this.accessCode = voucherCodeSearch.getAccessCode();
    this.totalReactivations = voucherCodeSearch.getTotalReactivations();

    this.batch = voucherCodeSearch.getBatch();
    this.quantity = voucherCodeSearch.getQuantity();
    this.createdBy = voucherCodeSearch.getCreatedBy();
    this.dateCreated = voucherCodeSearch.getDateCreated();
    this.updatedOn = voucherCodeSearch.getUpdatedOn();
    this.updatedBy = voucherCodeSearch.getUpdatedBy();
    this.lastActivatedBy = voucherCodeSearch.getLastActivatedBy();
    this.lastActivatedDate = voucherCodeSearch.getLastActivatedDate();

  }

  public Voucher() {
    super();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Voucher [id=" + id + ", voucherId=" + voucherId + ", voucherCode=" + voucherCode
        + ", startDate=" + startDate + ", endDate=" + endDate + ", type=" + type + ", empty="
        + isVoid + ", accessCode=" + accessCode + ", totalReactivations=" + totalReactivations
        + ", batch=" + batch + ", quantity=" + quantity + ", createdBy=" + createdBy
        + ", dateCreated=" + dateCreated + ", updatedOn=" + updatedOn + ", updatedBy=" + updatedBy
        + ", lastActivatedBy=" + lastActivatedBy + "]";
  }

  @MongoSource
  private String batch;
  @MongoSource
  @Min(value = 1, message = "quantity Cannot be less than {value}")
  private Integer quantity;
  @MongoSource
  @SamSource
  private String createdBy;
  @MongoSource
  @Pattern(regexp = "[0-9]*", message = "Enter Valid dateCreated")
  private String dateCreated;
  @MongoSource
  @SamSource
  @Pattern(regexp = "[0-9]*", message = "Enter Valid updatedOn")
  private String updatedOn;
  @MongoSource
  @SamSource
  private String updatedBy;

  @MongoSource
  @SamSource
  // @NotBlank(message="lastActivatedBy cannot be blank")
  private String lastActivatedBy;

  @MongoSource
  @SamSource
  @Pattern(regexp = "[0-9]*", message = "Enter Valid lastActivatedDate")
  // @NotBlank(message="lastActivatedDate cannot be blank")
  private String lastActivatedDate;

  /**
   * getId.
   * 
   * @return the id
   */
  @GraphQLIgnore
  public String getId() {
    return id;
  }

  /**
   * {enclosing_method}.
   * 
   * @param id
   *          the id to set
   */
  @GraphQLIgnore
  public void setId(String id) {
    this.id = id;
  }

  /**
   * getVoucherId.
   * 
   * @return the voucherId
   */
  public String getVoucherId() {
    return voucherId;
  }

  /**
   * {enclosing_method}.
   * 
   * @param voucherId
   *          the voucherId to set
   */
  public void setVoucherId(String voucherId) {
    this.voucherId = voucherId;
  }

  /**
   * getVoucherCode.
   * 
   * @return the voucherCode
   */
  public String getVoucherCode() {
    return voucherCode;
  }

  /**
   * {enclosing_method}.
   * 
   * @param voucherCode
   *          the voucherCode to set
   */
  public void setVoucherCode(String voucherCode) {
    this.voucherCode = voucherCode;
  }

  /**
   * getStartDate.
   * 
   * @return the startDate
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * {enclosing_method}.
   * 
   * @param startDate
   *          the startDate to set
   */
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  /**
   * getEndDate.
   * 
   * @return the endDate
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * {enclosing_method}.
   * 
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  /**
   * getType.
   * 
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * {enclosing_method}.
   * 
   * @param type
   *          the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * getAccessCode.
   * 
   * @return the accessCode
   */
  public String getAccessCode() {
    return accessCode;
  }

  /**
   * {enclosing_method}.
   * 
   * @param accessCode
   *          the accessCode to set
   */
  public void setAccessCode(String accessCode) {
    this.accessCode = accessCode;
  }

  /**
   * getTotalReactivations.
   * 
   * @return the totalReactivations
   */
  public Long getTotalReactivations() {
    return totalReactivations;
  }

  /**
   * {enclosing_method}.
   * 
   * @param totalReactivations
   *          the totalReactivations to set
   */
  public void setTotalReactivations(Long totalReactivations) {
    this.totalReactivations = totalReactivations;
  }

  /**
   * getBatch.
   * 
   * @return the batch
   */
  public String getBatch() {
    return batch;
  }

  /**
   * {enclosing_method}.
   * 
   * @param batch
   *          the batch to set
   */
  public void setBatch(String batch) {
    this.batch = batch;
  }

  /**
   * getCreatedBy.
   * 
   * @return the createdBy
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * {enclosing_method}.
   * 
   * @param createdBy
   *          the createdBy to set
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * getDateCreated.
   * 
   * @return the dateCreated
   */
  public String getDateCreated() {
    return dateCreated;
  }

  /**
   * {enclosing_method}.
   * 
   * @param dateCreated
   *          the dateCreated to set
   */
  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }

  /**
   * getUpdatedOn.
   * 
   * @return the updatedOn
   */
  public String getUpdatedOn() {
    return updatedOn;
  }

  /**
   * {enclosing_method}.
   * 
   * @param updatedOn
   *          the updatedOn to set
   */
  public void setUpdatedOn(String updatedOn) {
    this.updatedOn = updatedOn;
  }

  /**
   * getUpdatedBy.
   * 
   * @return the updatedBy
   */
  public String getUpdatedBy() {
    return updatedBy;
  }

  /**
   * {enclosing_method}.
   * 
   * @param updatedBy
   *          the updatedBy to set
   */
  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  /**
   * @return the quantity
   */
  public Integer getQuantity() {
    return quantity;
  }

  /**
   * @param quantity
   *          the quantity to set
   */
  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getLastActivatedBy() {
    return lastActivatedBy;
  }

  public void setLastActivatedBy(String lastActivatedBy) {
    this.lastActivatedBy = lastActivatedBy;
  }

  public Boolean getIsVoid() {
    return isVoid;
  }

  public void setIsVoid(Boolean isVoid) {
    this.isVoid = isVoid;
  }

  /**
   * @return the lastActivatedDate
   */
  public String getLastActivatedDate() {
    return lastActivatedDate;
  }

  /**
   * @param lastActivatedDate
   *          the lastActivatedDate to set
   */
  public void setLastActivatedDate(String lastActivatedDate) {
    this.lastActivatedDate = lastActivatedDate;
  }

}
