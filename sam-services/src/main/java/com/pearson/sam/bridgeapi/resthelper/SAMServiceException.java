/**
 * 
 */
package com.pearson.sam.bridgeapi.resthelper;

/**
 * @author VKuma09
 *
 */
public class SAMServiceException extends RuntimeException {

  /**
   * @param string
   */
  public SAMServiceException(String string) {
    super(string,new Throwable(string));
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

}
