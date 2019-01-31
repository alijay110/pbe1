package com.pearson.sam.bridgeapi.serviceimpl;

import com.pearson.sam.bridgeapi.iservice.IAppNotificationService;
import com.pearson.sam.bridgeapi.model.AppNotification;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.repository.AppNotificationRepository;
import com.pearson.sam.bridgeapi.util.Utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class AppNotificationsService implements IAppNotificationService{

	private static final Logger logger = LoggerFactory.getLogger(AppNotificationsService.class);
	
	@Autowired
	AppNotificationRepository appNotificationRepository;
	
	@Override
	public AppNotification create(AppNotification appNotification) {
		//logger.info("Creating AppNotification {}");
		AppNotification mergedData = appNotificationRepository.save(appNotification);
		return mergedData;
	}

	@Override
	public AppNotification update(AppNotification data) {
		AppNotification dbData = appNotificationRepository.findByNotificationId(data.getNotificationId());
		Utils.copyNonNullProperties(data, dbData);
		return  appNotificationRepository.save(dbData);
	}

	@Override
	public AppNotification fetchOne(AppNotification object) {
		return null;
	}

	@Override
	public List<AppNotification> fetchMultiple(KeyType key, List<String> ids) {
		return null;
	}

	@Override
	public AppNotification delete(AppNotification object) {
		return null;
	}
	
	@Override
	public Page<AppNotification> pageIt(Pageable pageable, Example<AppNotification> e) {
		    return appNotificationRepository.findAll(e, pageable);
	}

}