package com.pearson.sam.bridgeapi.model;

import java.util.List;

public class Description {
  
	private String audience;
	private String credit;
	private String author;
	private String acknowledgements;
	private List<String> keywords;
	private String isbn;
	private Boolean freeResource;
	
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	
	public String getAcknowledgements() {
		return acknowledgements;
	}
	public void setAcknowledgements(String acknowledgements) {
		this.acknowledgements = acknowledgements;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public Boolean getFreeResource() {
		return freeResource;
	}
	public void setFreeResource(Boolean freeResource) {
		this.freeResource = freeResource;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	
	
}
