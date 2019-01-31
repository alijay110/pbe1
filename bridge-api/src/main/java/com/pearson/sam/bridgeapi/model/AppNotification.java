package com.pearson.sam.bridgeapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.pearson.sam.bridgeapi.annotations.MongoSource;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.types.GraphQLType;

@Document(collection = "appNotification")
@GraphQLType(name = "AppNotification")
public class AppNotification extends BaseModel{	

	@Id
	private String id;
	
	@MongoSource
	private String notificationId;
	
	@MongoSource
	private String title;
	
	@MongoSource
	private String caption;
	
	@MongoSource
	private String dateTime;
	
	@MongoSource
	private String type;
	
	@MongoSource
	private Boolean isRead;
	
	@MongoSource
	private String user;

	@GraphQLIgnore
	public String getId() {
		return id;
	}

	@GraphQLIgnore
	public void setId(String id) {
		this.id = id;
	}

	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	
	
}