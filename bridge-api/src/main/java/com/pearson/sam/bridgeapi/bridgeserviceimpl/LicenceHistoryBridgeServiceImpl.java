package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHistoryBridgeService;
import com.pearson.sam.bridgeapi.iservice.ILicenceHistoryService;
import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

@Service
public class LicenceHistoryBridgeServiceImpl implements ILicenceHistoryBridgeService {
	private static final Logger logger = LoggerFactory.getLogger(LicenceHistoryBridgeServiceImpl.class);

	@Autowired
	private ILicenceHistoryService licenceHistoryService;

	@Autowired
	private ISessionFacade sessionFacade;

	@Override
	public LicenceHistory create(LicenceHistory licenceHistory) {
		User user = sessionFacade.getLoggedInUser(true);
		licenceHistory.setAlteredBy(user.getUid());
		return licenceHistoryService.create(licenceHistory);
	}

	@Override
	public Page<LicenceHistory> pageIt(Pageable pageable, Example<LicenceHistory> e) {
		// TODO Auto-generated method stub
		return licenceHistoryService.pageIt(pageable, e);
	}

}
