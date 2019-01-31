/**
 * 
 */
package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_ALTER_DATE;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;

/**
 * @author VGUDLSA
 *
 */
@Document(collection = "licenceHolderHistory")
public class LicenceHolderHistory extends BaseModel{
	
	@Id
	private String id;
	@MongoSource
	private String licenceHolderHistoryId;
	@MongoSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_ALTER_DATE)
	private String alterDate;
	@MongoSource
	private String alteredBy;
	@MongoSource
	//@DBRef
	private LicenceHolder alteration;
	
	
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
	/**
	 * @return the licenceHolderHistoryId
	 */
	public String getLicenceHolderHistoryId() {
		return licenceHolderHistoryId;
	}
	/**
	 * @param licenceHolderHistoryId the licenceHolderHistoryId to set
	 */
	public void setLicenceHolderHistoryId(String licenceHolderHistoryId) {
		this.licenceHolderHistoryId = licenceHolderHistoryId;
	}
	/**
	 * @return the alterDate
	 */
	public String getAlterDate() {
		return alterDate;
	}
	/**
	 * @param alterDate the alterDate to set
	 */
	public void setAlterDate(String alterDate) {
		this.alterDate = alterDate;
	}
	/**
	 * @return the alteredBy
	 */
	public String getAlteredBy() {
		return alteredBy;
	}
	/**
	 * @param alteredBy the alteredBy to set
	 */
	public void setAlteredBy(String alteredBy) {
		this.alteredBy = alteredBy;
	}
	/**
	 * @return the alteration
	 */
	public LicenceHolder getAlteration() {
		return alteration;
	}
	/**
	 * @param alteration the alteration to set
	 */
	public void setAlteration(LicenceHolder alteration) {
		this.alteration = alteration;
	}
	

}
