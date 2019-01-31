/**
 * 
 */
package com.pearson.sam.bridgeapi.constants;

/**
 * @author VGUDLSA
 *
 */
public interface ValidatorConstants {
	
	public static final String CODE_IN_BOOK ="In-book";
	
	public static final String CODE_TEACHER ="teacher";
	
	public static final String CODE_SINGLE_USE ="Single use";
	
	public static final String CODE_STUDENT ="student";
	
	public static final String TIME_ZONE ="Australia/Sydney";
	
	public static final String CODE_EXPIRE = "Expired : Looks like this code is expired. You can buy a new product from pearson.com.au";
	
	public static final String ALREADY_ACTIVATED ="Information : You've already activated this code. You can access all your products from the bookshelf.";
	
	public static final String IN_USE ="Information : Looks like this code is already in use. You can buy a new one from pearson.com.au";
	
	public static final String TEACHER_STATUS ="Information : We are still waiting to confirm your teacher's status, you will not be able to activate teacher's products until we do so.";

	public static final String INVALID_CODE ="Not In database : Looks like that access code is invalid. Please try again.";
	
	public static final String ACCESSCODE_FIRST ="Voucher Code : Looks like you entered a voucher code. Please enter an access code first.";
	
	public static final String NO_PERMISSIONS ="Information : Looks like you don't have permission to access this code. If you believe this is a mistake, please contact support.";
	
	public static final String INVALID_VOUCHER_CODE ="Not In database : Looks like that voucher code is invalid. Please try again.";
	
	public static final String WRONG_VOUCHER_CODE = "Wrong voucher code type : Looks like the this is an incorrect voucher type. You can buy a new voucher from pearson.com.au";
	
	public static final String NOT_AUTHORIZED ="Not Authorised : Looks like you are not authorized to activate the code.";
}
