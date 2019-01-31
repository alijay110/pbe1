package com.pearson.sam.bridgeapi.model;

import java.io.Serializable;

/**
 * Program model.
 * @author VGUDLSA
 *
 */
public class Program implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 9126081449570120006L;
  /**
   * 
   */

  private String affiliationCode;
  private String affiliationName;
  private String educationLevelCode;
  private String educationLevelDescription;

  /**
   * returns the affiliationCode.
   * 
   * @return the affiliationCode
   */
  public String getAffiliationCode() {
    return affiliationCode;
  }

  /**
   * the affiliationCode to set.
   * 
   * @param affiliationCode
   *          the affiliationCode to set
   */
  public void setAffiliationCode(String affiliationCode) {
    this.affiliationCode = affiliationCode;
  }

  /**
   * the affiliationName.
   * 
   * @return the affiliationName
   */
  public String getAffiliationName() {
    return affiliationName;
  }

  /**
   * the affiliationName to set.
   * 
   * @param affiliationName
   *          a.
   */
  public void setAffiliationName(String affiliationName) {
    this.affiliationName = affiliationName;
  }

  /**
   * returns the educationLevelCode.
   * 
   * @return the educationLevelCode
   */
  public String getEducationLevelCode() {
    return educationLevelCode;
  }

  /**
   * the educationLevelCode to set.
   * 
   * @param educationLevelCode
   *          e
   */
  public void setEducationLevelCode(String educationLevelCode) {
    this.educationLevelCode = educationLevelCode;
  }

  /**
   * returns the educationLevelDescription e.
   * 
   * @return the educationLevelDescription e
   */
  public String getEducationLevelDescription() {
    return educationLevelDescription;
  }

  /**
   * the educationLevelDescription to set.
   * 
   * @param educationLevelDescription
   *          e
   */
  public void setEducationLevelDescription(String educationLevelDescription) {
    this.educationLevelDescription = educationLevelDescription;
  }

}
