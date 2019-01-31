package com.pearson.sam.bridgeapi.model;


import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.BUNDLE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_CREATEDON;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.ENTER_VALID_UPDATEDON;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCTMODEL_SHOULD_CONTAIN;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.PRODUCTNAME_SHOULD_CONTAIN;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.SUBJECT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;
import static com.pearson.sam.bridgeapi.constants.ErrorMessageConstants.TYPE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER;

import java.util.List;

import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;
import com.pearson.sam.bridgeapi.annotations.SamSource;
import com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch;
import com.pearson.sam.bridgeapi.validators.ValidateModelCollection;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLInputField;

@Document(collection = "product")
public class Product extends BaseModel {

	@Id
	private String id;
	@MongoSource
	private String productId;
	@MongoSource
	private List<String> type;
	@MongoSource
	private Details details;
	@SamSource
	private String isbn;
	@SamSource
	private String activeTimestamp;
	@SamSource
	@Pattern(regexp = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", message = "Enter Valid courseUrl")
	private String courseUrl;
	@MongoSource
	@SamSource
	private String createdBy;
	@MongoSource
	@SamSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_CREATEDON)
	private String createdOn;
	@SamSource
	private String inactiveTimestamp;
	@SamSource
	private String isPartOf;
	@SamSource
	@Pattern(regexp = "^[0-9a-zA-Z.'-]*", message = PRODUCTMODEL_SHOULD_CONTAIN)
	private String productModel;
	@SamSource
	@Pattern(regexp = "^[0-9a-zA-Z][0-9a-z A-Z.'-]*", message = PRODUCTNAME_SHOULD_CONTAIN)
	private String productName;
	@SamSource
	private String productType;
	@SamSource
	private String publishedOn;
	@SamSource
	@ValidateModelCollection(message = SUBJECT_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> subjects;
	@SamSource
	private String thumbnailUrl;
	@MongoSource
	@SamSource
	private String updatedBy;
	@MongoSource
	@SamSource
	@Pattern(regexp = "[0-9]*", message = ENTER_VALID_UPDATEDON )
	private String updatedOn;
	@SamSource
	private List<Program> programs;

	@MongoSource
	private String reactivationVoucherCodeType;

	@MongoSource
	@ValidateModelCollection(message = BUNDLE_SHOULD_NOT_CONTAIN_SPECIAL_CHARACTER)
	private List<String> bundle;

	@SamSource
	@MongoSource
	private String productCode;

	@MongoSource
	private Boolean isTeacherProduct;

	@MongoSource
	private String productTerm;

	@MongoSource
	private String series;

	@MongoSource
	private String template;

	@MongoSource
	private String status;

	@MongoSource
	private List<String> defaultYearLevel;

	@MongoSource
	private String coolingOffPeriod;

	@MongoSource
	private String monthUntilReactivation;

	@MongoSource
	private String cartridgeId;
	
	@SamSource
	private List<String> resourceCourseIds;
	
	@MongoSource
	private String linkedProductCode;
	
	private Long resourcesCount;

	@GraphQLIgnore
	public String getId() {
		return id;
	}

	public Product(String id, String productId, List<String> type, Details details, String isbn, String activeTimestamp,
			String courseUrl, String createdBy, String createdOn, String inactiveTimestamp, String isPartOf,
			String productModel, String productName, String productType, String publishedOn, List<String> subjects,
			String thumbnailUrl, String updatedBy, String updatedOn, List<Program> programs,
			String reactivationVoucherCodeType, List<String> bundle, String productCode, Boolean isTeacherProduct,
			String productTerm, String series, String template, String status, List<String> defaultYearLevel,
			String coolingOffPeriod, String monthUntilReactivation, String cartridgeId,String linkedProductCode,Long resourcesCount) {
		super();
		this.id = id;
		this.productId = productId;
		this.type = type;
		this.details = details;
		this.isbn = isbn;
		this.activeTimestamp = activeTimestamp;
		this.courseUrl = courseUrl;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.inactiveTimestamp = inactiveTimestamp;
		this.isPartOf = isPartOf;
		this.productModel = productModel;
		this.productName = productName;
		this.productType = productType;
		this.publishedOn = publishedOn;
		this.subjects = subjects;
		this.thumbnailUrl = thumbnailUrl;
		this.updatedBy = updatedBy;
		this.updatedOn = updatedOn;
		this.programs = programs;
		this.reactivationVoucherCodeType = reactivationVoucherCodeType;
		this.bundle = bundle;
		this.productCode = productCode;
		this.isTeacherProduct = isTeacherProduct;
		this.productTerm = productTerm;
		this.series = series;
		this.template = template;
		this.status = status;
		this.defaultYearLevel = defaultYearLevel;
		this.coolingOffPeriod = coolingOffPeriod;
		this.monthUntilReactivation = monthUntilReactivation;
		this.cartridgeId = cartridgeId;
		this.linkedProductCode = linkedProductCode;
		this.resourcesCount = resourcesCount;
	}

	/**
	 * 
	 */
	public Product() {
		super();
	}

	public void replaceWith(Product product) {
		this.id = product.getId();
		this.productId = product.getProductId();
		this.type = product.getType();
		this.details = product.getDetails();
		this.isbn = product.getIsbn();
		this.activeTimestamp = product.getActiveTimestamp();
		this.courseUrl = product.getCourseUrl();
		this.createdBy = product.getCreatedBy();
		this.createdOn = product.getCreatedOn();
		this.inactiveTimestamp = product.getInactiveTimestamp();
		this.isPartOf = product.getIsPartOf();
		this.productModel = product.getProductModel();
		this.productName = product.getProductName();
		this.productType = product.getProductType();
		this.publishedOn = product.getPublishedOn();
		this.subjects = product.getSubjects();
		this.thumbnailUrl = product.getThumbnailUrl();
		this.updatedBy = product.getUpdatedBy();
		this.updatedOn = product.getUpdatedOn();
		this.programs = product.getPrograms();
		this.reactivationVoucherCodeType = product.getReactivationVoucherCodeType();
		this.bundle = product.getBundle();
		this.productCode = product.getProductCode();
		this.isTeacherProduct = product.getIsTeacherProduct();
		this.productTerm = product.getProductTerm();
		this.series = product.getSeries();
		this.template = product.getTemplate();
		this.status = product.getStatus();
		this.defaultYearLevel = product.getDefaultYearLevel();
		this.coolingOffPeriod = product.getCoolingOffPeriod();
		this.monthUntilReactivation = product.getMonthUntilReactivation();
		this.cartridgeId = product.getCartridgeId();
		this.linkedProductCode = product.getLinkedProductCode();
		this.resourcesCount = product.getResourcesCount();
	}

	 public Product(ProductSearch productSearch) {
	    this.id = productSearch.getId();
	    this.productId = productSearch.getProductId();
	    this.type = productSearch.getType();
	    this.details = productSearch.getDetails();
	    this.courseUrl = productSearch.getCourseUrl();
	    this.createdOn = productSearch.getCreatedOn();
	    this.updatedBy = productSearch.getUpdatedBy();
	    this.updatedOn = productSearch.getUpdatedOn();
	    this.reactivationVoucherCodeType = productSearch.getReactivationVoucherCodeType();
	    this.productCode = productSearch.getProductCode();
	    this.isTeacherProduct = productSearch.getIsTeacherProduct();
	    this.productTerm = productSearch.getProductTerm();
	    this.series = productSearch.getSeries();
	    this.template = productSearch.getTemplate();
	    this.status = productSearch.getStatus();
	    this.defaultYearLevel = productSearch.getDefaultYearLevel();
	    this.coolingOffPeriod = productSearch.getCoolingOffPeriod();
	    this.monthUntilReactivation = productSearch.getMonthUntilReactivation();
	    this.cartridgeId = productSearch.getCartridgeId();
	    this.linkedProductCode = productSearch.getLinkedProductCode();
	    this.resourcesCount = productSearch.getResourcesCount();
	  }

	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}

	/**
	 * returns the iSBN.
	 */
	public @GraphQLInputField(name = "ISBN") String getIsbn() {
		return isbn;
	}

	/**
	 * param iSBN. the iSBN to set
	 */
	public void setIsbn(@GraphQLInputField(name = "ISBN") String iSBN) {
		isbn = iSBN;
	}

	/**
	 * returns the activeTimestamp.
	 */
	public String getActiveTimestamp() {
		return activeTimestamp;
	}

	/**
	 * param activeTimestamp. the activeTimestamp to set
	 */
	public void setActiveTimestamp(String activeTimestamp) {
		this.activeTimestamp = activeTimestamp;
	}

	/**
	 * returns the courseUrl.
	 */
	public String getCourseUrl() {
		return courseUrl;
	}

	/**
	 * param courseUrl. the courseUrl to set
	 */
	public void setCourseUrl(String courseUrl) {
		this.courseUrl = courseUrl;
	}

	/**
	 * returns the createdBy.
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * param createdBy. the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * returns the createdOn.
	 */
	public String getCreatedOn() {
		return createdOn;
	}

	/**
	 * param createdOn. the createdOn to set
	 */
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * returns the inactiveTimestamp.
	 */
	public String getInactiveTimestamp() {
		return inactiveTimestamp;
	}

	/**
	 * param inactiveTimestamp. the inactiveTimestamp to set
	 */
	public void setInactiveTimestamp(String inactiveTimestamp) {
		this.inactiveTimestamp = inactiveTimestamp;
	}

	/**
	 * returns the isPartOf.
	 */
	public String getIsPartOf() {
		return isPartOf;
	}

	/**
	 * param isPartOf. the isPartOf to set
	 */
	public void setIsPartOf(String isPartOf) {
		this.isPartOf = isPartOf;
	}

	/**
	 * returns the productModel.
	 */
	public String getProductModel() {
		return productModel;
	}

	/**
	 * param productModel. the productModel to set
	 */
	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	/**
	 * returns the productName.
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * param productName. the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * returns the productType.
	 */
	public String getProductType() {
		return productType;
	}

	/**
	 * param productType. the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * returns the publishedOn.
	 */
	public String getPublishedOn() {
		return publishedOn;
	}

	/**
	 * param publishedOn. the publishedOn to set
	 */
	public void setPublishedOn(String publishedOn) {
		this.publishedOn = publishedOn;
	}

	/**
	 * returns the subjects.
	 */
	public List<String> getSubjects() {
		return subjects;
	}

	/**
	 * param subjects. the subjects to set
	 */
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}

	/**
	 * returns the thumbnailUrl.
	 */
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	/**
	 * param thumbnailUrl. the thumbnailUrl to set
	 */
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	/**
	 * returns the updatedBy.
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * param updatedBy. the updatedBy to set.
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * returns the updatedOn.
	 */
	public String getUpdatedOn() {
		return updatedOn;
	}

	/**
	 * the updatedOn to set.
	 */
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	/**
	 * the programs.
	 */
	public List<Program> getPrograms() {
		return programs;
	}

	/**
	 * the programs to set.
	 */
	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}

	public String getReactivationVoucherCodeType() {
		return reactivationVoucherCodeType;
	}

	public void setReactivationVoucherCodeType(String reactivationVoucherCodeType) {
		this.reactivationVoucherCodeType = reactivationVoucherCodeType;
	}

	public List<String> getBundle() {
		return bundle;
	}

	public void setBundle(List<String> bundle) {
		this.bundle = bundle;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Boolean getIsTeacherProduct() {
		return isTeacherProduct;
	}

	public void setIsTeacherProduct(Boolean isTeacherProduct) {
		this.isTeacherProduct = isTeacherProduct;
	}

	public String getProductTerm() {
		return productTerm;
	}

	public void setProductTerm(String productTerm) {
		this.productTerm = productTerm;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getDefaultYearLevel() {
		return defaultYearLevel;
	}

	public void setDefaultYearLevel(List<String> defaultYearLevel) {
		this.defaultYearLevel = defaultYearLevel;
	}

	public String getCoolingOffPeriod() {
		return coolingOffPeriod;
	}

	public void setCoolingOffPeriod(String coolingOffPeriod) {
		this.coolingOffPeriod = coolingOffPeriod;
	}

	public String getMonthUntilReactivation() {
		return monthUntilReactivation;
	}

	public void setMonthUntilReactivation(String monthUntilReactivation) {
		this.monthUntilReactivation = monthUntilReactivation;
	}

	public String getCartridgeId() {
		return cartridgeId;
	}

	public void setCartridgeId(String cartridgeId) {
		this.cartridgeId = cartridgeId;
	}

	public List<String> getResourceCourseIds() {
		return resourceCourseIds;
	}

	public void setResourceCourseIds(List<String> resourceCourseIds) {
		this.resourceCourseIds = resourceCourseIds;
	}

	public String getLinkedProductCode() {
		return linkedProductCode;
	}

	public void setLinkedProductCode(String linkedProductCode) {
		this.linkedProductCode = linkedProductCode;
	}

	public Long getResourcesCount() {
		return resourcesCount;
	}
	
	@GraphQLIgnore
	public void setResourcesCount(Long resourcesCount) {
		this.resourcesCount = resourcesCount;
	}
	
	

}
