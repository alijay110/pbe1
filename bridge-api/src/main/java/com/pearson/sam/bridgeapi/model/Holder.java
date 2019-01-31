/**
 * 
 */
package com.pearson.sam.bridgeapi.model;

import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.QUANTITY_SHOULD_CONTAIN_ONLY_NUMBER;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;

/**
 * @author VGUDLSA
 *
 */

public class Holder{
	
	@Id
	private String id;
	@MongoSource
	private School school;
	@MongoSource
	private Product product;
	@MongoSource
	private ChannelPartner organisation;
	@MongoSource
	@Pattern(regexp = "^[0-9]*", message = QUANTITY_SHOULD_CONTAIN_ONLY_NUMBER)
	private String quantity;
	@MongoSource
	private Boolean teacherAccess;
	@MongoSource
	private Integer totalLicencesInUse;
	@MongoSource
	private String schoolProductType;
	
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
	 * @return the school
	 */
	public School getSchool() {
		return school;
	}
	/**
	 * @param school the school to set
	 */
	public void setSchool(School school) {
		this.school = school;
	}
	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	/**
	 * @return the organisation
	 */
	public ChannelPartner getOrganisation() {
		return organisation;
	}
	/**
	 * @param organisation the organisation to set
	 */
	public void setOrganisation(ChannelPartner organisation) {
		this.organisation = organisation;
	}
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the teacherAccess
	 */
	public Boolean getTeacherAccess() {
		return teacherAccess;
	}
	/**
	 * @param teacherAccess the teacherAccess to set
	 */
	
	public void setTeacherAccess(Boolean teacherAccess) {
		this.teacherAccess = teacherAccess;
	}
	public Integer getTotalLicencesInUse() {
		return totalLicencesInUse;
	}
	public void setTotalLicencesInUse(Integer totalLicencesInUse) {
		this.totalLicencesInUse = totalLicencesInUse;
	}
	
	public String getSchoolProductType() {
		return schoolProductType;
	}
	public void setSchoolProductType(String schoolProductType) {
		this.schoolProductType = schoolProductType;
	}
	
}
