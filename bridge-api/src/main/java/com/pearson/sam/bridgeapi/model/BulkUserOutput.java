package com.pearson.sam.bridgeapi.model;

public class BulkUserOutput extends User {
  private String userAdded;
  private String errorText;

  public BulkUserOutput() {

  }

  public BulkUserOutput(String userAdded, String errorText) {
    this.userAdded = userAdded;
    this.errorText = errorText;
  }

  public String getUserAdded() {
    return userAdded;
  }

  public void setUserAdded(String userAdded) {
    this.userAdded = userAdded;
  }

  public String getErrorText() {
    return errorText;
  }

  public void setErrorText(String errorText) {
    this.errorText = errorText;
  }

  @Override
  public String toString() {
    return "BulkUserOutput [userAdded=" + userAdded + ", errorText=" + errorText + ", getUid()="
        + getUid() + ", getFullName()=" + getFullName() + ", getRoles()=" + getRoles()
        + ", getAvatar()=" + getAvatar() + ", getTandc()=" + getTandc() + ", getEmail()="
        + getEmail() + ", getUserName()=" + getUserName() + ", getPreferredName()="
        + getPreferredName() + ", getPasswd()=" + getPasswd() + ", getPasswdValidation()="
        + getPasswdValidation() + ", getYear()=" + getYear() + ", getProduct()=" + getProduct()
        + ", getClassRoom()=" + getClassroom() + ", getSchool()=" + getSchool() + ", getOrganisation()="
        + getOrganisation() + ", getStatus()=" + getStatus() + ", getPortal()=" + getPortal()
        + ", isSso()=" + isSso() + ", getLastLogin()=" + getLastLogin() + ", getDateCreated()="
        + getDateCreated() + ", getProfileOptions()=" + getSubjectPreference() + ", getMessage()="
        + getMessage() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
        + ", toString()=" + super.toString() + "]";
  }

}
