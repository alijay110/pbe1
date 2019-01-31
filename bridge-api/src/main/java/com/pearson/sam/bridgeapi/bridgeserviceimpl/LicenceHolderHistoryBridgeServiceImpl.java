package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHolderHistoryBridgeService;
import com.pearson.sam.bridgeapi.iservice.ILicenceHolderHistoryService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.LicenceHolderHistory;

@Service
public class LicenceHolderHistoryBridgeServiceImpl implements ILicenceHolderHistoryBridgeService {
	private static final Logger logger = LoggerFactory.getLogger(LicenceHolderHistoryBridgeServiceImpl.class);

	@Autowired
	private ILicenceHolderHistoryService licenceHolderHistoryService;
	@Override
	public LicenceHolderHistory create(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		return licenceHolderHistoryService.create(licenceHolderHistory);
	}

	@Override
	public LicenceHolderHistory update(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		return licenceHolderHistoryService.update(licenceHolderHistory);
	}

	@Override
	public LicenceHolderHistory fetch(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		return licenceHolderHistoryService.fetch(licenceHolderHistory);
	}

	@Override
	public List<LicenceHolderHistory> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return licenceHolderHistoryService.fetchMultiple(key, ids);
	}

	@Override
	public LicenceHolderHistory delete(LicenceHolderHistory licenceHolderHistory) {
		// TODO Auto-generated method stub
		return licenceHolderHistoryService.delete(licenceHolderHistory);
	}

	@Override
	public Page<LicenceHolderHistory> pageIt(Pageable pageable, Example<LicenceHolderHistory> e) {
		// TODO Auto-generated method stub
		return licenceHolderHistoryService.pageIt(pageable, e);
	}

}
