package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.model.AppNotification;
import com.pearson.sam.bridgeapi.model.KeyType;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAppNotificationBridgeService {

  AppNotification create(AppNotification appNotification);

  AppNotification update(AppNotification object);

  AppNotification fetchOne(AppNotification object);

  List<AppNotification> fetchMultiple(KeyType key, List<String> ids);

  AppNotification delete(AppNotification object);

  Page<AppNotification> pageIt(Pageable pageable, Example<AppNotification> e);

}
