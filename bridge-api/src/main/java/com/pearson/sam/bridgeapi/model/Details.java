package com.pearson.sam.bridgeapi.model;

import java.io.Serializable;
import java.util.List;

public class Details implements Serializable{


  /**
   * 
   */
  private static final long serialVersionUID = 6405095295919406177L;
  	private String title;
	private String edition;
	private String description;
	private String cover;
	private List<String> subject;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<String> getSubject() {
		return subject;
	}

	public void setSubject(List<String> subject) {
		this.subject = subject;
	}

}
