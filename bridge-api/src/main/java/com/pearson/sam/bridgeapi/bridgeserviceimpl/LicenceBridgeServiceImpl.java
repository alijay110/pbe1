package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.enums.AccessCodeType;
import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceBridgeService;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeRepoService;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService;
import com.pearson.sam.bridgeapi.iservice.ILicenceService;
import com.pearson.sam.bridgeapi.iservice.ISubscriptionService;
import com.pearson.sam.bridgeapi.model.AccessCodes;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.Licence;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import io.jsonwebtoken.lang.Collections;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
@Service
public class LicenceBridgeServiceImpl implements ILicenceBridgeService{
	private static final Logger logger = LoggerFactory.getLogger(LicenceBridgeServiceImpl.class);

	@Autowired
	private ILicenceService licenceService;
	
	@Autowired
	private ISessionFacade sessionFacade;
	
	@Autowired
	IAccessCodeRepoService accessCodeRepoService;
	
	@Autowired
	ISubscriptionService subscriptionService;
	
	@Override
	public Licence create(Licence licence) {
		// TODO Auto-generated method stub
		User user = sessionFacade.getLoggedInUser(true);
		licence.setCreatedBy(user.getUid());
		Licence response =  licenceService.create(licence);
		//logger.info("added licence"+response);
		AccessCodes accesscode = new AccessCodes();
		accesscode.setProducts(response.getProducts());
		accesscode.setCreatedBy(user.getUid());
		accesscode.setType(AccessCodeType.SINGLE_USE.getType());
		accesscode.setQuantity(1);
		List<AccessCodes> accessCodes = accessCodeRepoService.generateAccessCodes(accesscode );
		if(!Collections.isEmpty(accessCodes)) {
			for(AccessCodes acccode : accessCodes) {
				//clarify it one time
				acccode.setUserName(licence.getAttachedUser());
				acccode.setSubscriptionDate(licence.getActivationDate());
				acccode.setStartDate(licence.getActivationDate());
				acccode.setEndDate(licence.getExpirationDate());
				subscriptionService.createAccessCodeSubscription(acccode);
				//logger.info("subscripiton processed....");
			}
		}
		return response;
	}

	@Override
	public Licence update(Licence licence) {
		// TODO Auto-generated method stub
		User user = sessionFacade.getLoggedInUser(true);
		licence.setCreatedBy(user.getUid());
		return licenceService.update(licence);
	}

	@Override
	public Licence fetch(Licence licence) {
		// TODO Auto-generated method stub
		return licenceService.fetch(licence);
	}

	@Override
	public Licence delete(Licence licence) {
		// TODO Auto-generated method stub
		return licenceService.delete(licence);
	}

	@Override
	public List<Licence> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return licenceService.fetchMultiple(key, ids);
	}

	@Override
	public Page<Licence> pageIt(Pageable pageable, Example<Licence> e) {
		// TODO Auto-generated method stub
		return licenceService.pageIt(pageable, e);
	}

}
