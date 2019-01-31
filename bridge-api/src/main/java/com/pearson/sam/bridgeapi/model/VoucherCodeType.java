/**
 * 
 */
package com.pearson.sam.bridgeapi.model;

/**
 * @author VGUDLSA
 *
 */
public enum VoucherCodeType {
	SEVENTOTEN("7-10"),ELEVENTOTWELVE("11-12"),VCEENGLISH("VCE English"),EXAMGUIDE("Exam Guide");
	
	private String action;
	
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}


	private VoucherCodeType(String action){
		this.action = action;
	}

}
