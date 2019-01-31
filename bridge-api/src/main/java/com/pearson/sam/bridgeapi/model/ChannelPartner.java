/**
 * 
 */
package com.pearson.sam.bridgeapi.model;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;

import org.springframework.data.annotation.Id;

/**
 * @author VGUDLSA
 *
 */
public class ChannelPartner extends BaseModel{
	
	@Id
	private String id;
	@MongoSource
	private String name;
	
	@MongoSource
	private String organisationId;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the organisationId
	 */
	public String getOrganisationId() {
		return organisationId;
	}

	/**
	 * @param organisationId the organisationId to set
	 */
	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}

	/**
	 * @return the id
	 */
	@GraphQLIgnore
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}
	
	

}
