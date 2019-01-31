/**
 * 
 */
package com.pearson.sam.bridgeapi.model;

import org.springframework.data.annotation.Id;

/**
 * @author VGUDLSA
 *
 */
public class VoucherCodeFee {
	
	@Id
	private String id;
	
	private String vceEnglish;
	
	private String examGuide;
	
	private String sevenToTen;
	
	private String elevenToTwelve;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the vceEnglish
	 */
	public String getVceEnglish() {
		return vceEnglish;
	}

	/**
	 * @param vceEnglish the vceEnglish to set
	 */
	public void setVceEnglish(String vceEnglish) {
		this.vceEnglish = vceEnglish;
	}

	/**
	 * @return the examGuide
	 */
	public String getExamGuide() {
		return examGuide;
	}

	/**
	 * @param examGuide the examGuide to set
	 */
	public void setExamGuide(String examGuide) {
		this.examGuide = examGuide;
	}

	/**
	 * @return the sevenToTen
	 */
	public String getSevenToTen() {
		return sevenToTen;
	}

	/**
	 * @param sevenToTen the sevenToTen to set
	 */
	public void setSevenToTen(String sevenToTen) {
		this.sevenToTen = sevenToTen;
	}

	/**
	 * @return the elevenToTwelve
	 */
	public String getElevenToTwelve() {
		return elevenToTwelve;
	}

	/**
	 * @param elevenToTwelve the elevenToTwelve to set
	 */
	public void setElevenToTwelve(String elevenToTwelve) {
		this.elevenToTwelve = elevenToTwelve;
	}
	
	
	

}
