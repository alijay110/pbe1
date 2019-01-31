package com.pearson.sam.bridgeapi.repository;

import com.pearson.sam.bridgeapi.model.AppNotification;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppNotificationRepository extends MongoRepository<AppNotification, String> {

	public AppNotification findByNotificationId(String id);


}