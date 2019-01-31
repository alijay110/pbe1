package com.pearson.sam.bridgeapi.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

@Document(collection = "licenceHistory")
public class LicenceHistory extends BaseModel{
  @Id
  private String id;

  @MongoSource
  String licenceHistoryId;

  @MongoSource
  String licence;

  @MongoSource
  @NotNull(message = "alteration should be present!")
  Licence alteration;

  @MongoSource
  @Pattern(regexp = "[0-9]*", message = "Enter Valid alterDate")
  String alterDate;

  @MongoSource
  String alteredBy;

  public String getLicenceHistoryId() {
    return licenceHistoryId;
  }

  public void setLicenceHistoryId(String licenceHistoryId) {
    this.licenceHistoryId = licenceHistoryId;
  }

  public String getLicence() {
    return licence;
  }

  public void setLicence(String licence) {
    this.licence = licence;
  }

  public Licence getAlteration() {
    return alteration;
  }

  public void setAlteration(Licence alteration) {
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
