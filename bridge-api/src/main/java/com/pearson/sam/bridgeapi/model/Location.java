package com.pearson.sam.bridgeapi.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

@Document(collection = "location")
public class Location extends BaseModel {

   @MongoSource
   private String suburb;
   @MongoSource
   private String postcode;
   @MongoSource
   private String state;
	
   
   /**
	 * @return the suburb
	 */
	public String getSuburb() {
		return suburb;
	}
	/**
	 * @param suburb the suburb to set
	 */
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}
	/**
	 * @param postcode the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
  
}
