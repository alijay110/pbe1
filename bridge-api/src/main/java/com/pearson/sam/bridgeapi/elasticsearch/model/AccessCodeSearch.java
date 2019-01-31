/**
 * 
 */
package com.pearson.sam.bridgeapi.elasticsearch.model;

import com.pearson.sam.bridgeapi.model.BaseModel;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author VGUDLSA
 *
 */
@Getter @Setter
@Document(indexName = "pp2accesscodes", type = "accesscode")
public class AccessCodeSearch extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	@Field(fielddata=true,type=FieldType.Text)
	private String accessCode;
	
	@Field(fielddata=true,type=FieldType.Text)
	private String code;

	private List<String> products;

	@Field(fielddata=true,type=FieldType.Text)
	private String startDate;

	@Field(fielddata=true,type=FieldType.Text)
	private String endDate;

	@Field(fielddata=true,type=FieldType.Text)
	private String type;

	private Boolean isVoid;
	
	private Integer totalReactivations;

	@Field(fielddata=true,type=FieldType.Text)
	private String batch;

	private Integer quantity;

	@Field(fielddata=true,type=FieldType.Text)
	private String createdBy;

	@Field(fielddata=true,type=FieldType.Text)
	private String dateCreated;

	@Field(fielddata=true,type=FieldType.Text)
	private String lastActivatedBy;

	@Field(fielddata=true,type=FieldType.Text)
	private String lastActivatedDate;

	@Field(fielddata=true,type=FieldType.Text)
	private String subscriptionDate;

	@Field(fielddata=true,type=FieldType.Text)
	private String subscriptionIdentifier;

	@Field(fielddata=true,type=FieldType.Text)
	private String userName;
	 
	 

}
