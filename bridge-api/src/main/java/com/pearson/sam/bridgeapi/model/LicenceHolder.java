/**
 * 
 */
package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_CREATED_DATE;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;

/**
 * @author VGUDLSA
 *
 */
@Document(collection = "licenceHolder")
public class LicenceHolder extends BaseModel {

  @Id
  private String id;
  @MongoSource
  private String licenceHolderId;
  @MongoSource
  @Valid
  private Holder holder;

  @MongoSource
  @Pattern(regexp = "[0-9]*", message = ENTER_VALID_CREATED_DATE)
  private String createdDate;
  @MongoSource
  private String createdBy;

  /**
   * @return the id
   */
  @GraphQLIgnore
  public String getId() {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  @GraphQLIgnore
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the licenceHolderId
   */
  public String getLicenceHolderId() {
    return licenceHolderId;
  }

  /**
   * @param licenceHolderId
   *          the licenceHolderId to set
   */
  public void setLicenceHolderId(String licenceHolderId) {
    this.licenceHolderId = licenceHolderId;
  }

  /**
   * @return the holder
   */
  public Holder getHolder() {
    return holder;
  }

  /**
   * @param holder
   *          the holder to set
   */
  public void setHolder(Holder holder) {
    this.holder = holder;
  }

  /**
   * getCreatedDate.
   * @return the createdDate
   */
  public String getCreatedDate() {
    return createdDate;
  }

  /**
   * {enclosing_method}.
   * @param createdDate the createdDate to set
   */
  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  /**
   * getCreatedBy.
   * @return the createdBy
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * {enclosing_method}.
   * @param createdBy the createdBy to set
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

}
