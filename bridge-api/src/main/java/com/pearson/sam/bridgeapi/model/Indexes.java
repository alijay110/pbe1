/**
 * 
 */
package com.pearson.sam.bridgeapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author VKuma09
 *
 */

@Document(collection = "indexes")
public class Indexes {

  @Id
  private String id;

  private String seqName;

  private int counter;

  

  /**
   * getId.
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * {enclosing_method}.
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * getSeqName.
   * 
   * @return the seqName
   */
  public String getSeqName() {
    return seqName;
  }

  /**
   * {enclosing_method}.
   * 
   * @param seqName
   *          the seqName to set
   */
  public void setSeqName(String seqName) {
    this.seqName = seqName;
  }

  /**
   * getCounter.
   * 
   * @return the counter
   */
  public int getCounter() {
    return counter;
  }

  /**
   * {enclosing_method}.
   * 
   * @param counter
   *          the counter to set
   */
  public void setCounter(int counter) {
    this.counter = counter;
  }

}
