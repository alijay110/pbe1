package com.pearson.sam.bridgeapi.model;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resource")
public class Resource extends BaseModel{

	@Id
	private String id;
	
	@MongoSource
	private String resourceId;
	@MongoSource
	private String productId;
	
	@MongoSource
	private Content content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	 

	
	
	
}