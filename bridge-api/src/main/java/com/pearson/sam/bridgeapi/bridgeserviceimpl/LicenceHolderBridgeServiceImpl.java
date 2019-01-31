package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.ibridgeservice.ILicenceHolderBridgeService;
import com.pearson.sam.bridgeapi.iservice.ILicenceHolderService;
import com.pearson.sam.bridgeapi.model.KeyType;
import com.pearson.sam.bridgeapi.model.LicenceHolder;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.serviceimpl.IndexService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LicenceHolderBridgeServiceImpl implements ILicenceHolderBridgeService {
	private static final Logger logger = LoggerFactory.getLogger(LicenceHolderBridgeServiceImpl.class);

	private static final String LICENSE_HOLDER_ID = "licenceHolderId";
	private static final String PP2LICENCEHOLDER = "PP2LICENCEHOLDER";

	@Autowired
	private ILicenceHolderService licenceHolderService;

	@Autowired
	private IndexService indexService;

	@Autowired
	private ISessionFacade sessionFacde;

	@Override
	public LicenceHolder create(LicenceHolder licenceHolder) {
		// TODO Auto-generated method stub
		licenceHolder.setLicenceHolderId(PP2LICENCEHOLDER + indexService.getNextId(LICENSE_HOLDER_ID));
		User user = sessionFacde.getLoggedInUser(true);
		licenceHolder.setCreatedBy(user.getUid());
		return licenceHolderService.create(licenceHolder);
	}

	@Override
	public LicenceHolder update(LicenceHolder licenceHolder,String operation) {
		// TODO Auto-generated method stub
		return licenceHolderService.update(licenceHolder,operation);
	}

	@Override
	public LicenceHolder fetch(LicenceHolder licenceHolder) {
		// TODO Auto-generated method stub
		return licenceHolderService.fetch(licenceHolder);
	}

	@Override
	public List<LicenceHolder> fetchMultiple(KeyType key, List<String> ids) {
		// TODO Auto-generated method stub
		return licenceHolderService.fetchMultiple(key, ids);
	}

	@Override
	public LicenceHolder delete(LicenceHolder licenceHolder) {
		// TODO Auto-generated method stub
		return licenceHolderService.delete(licenceHolder);
	}

	@Override
	public Page<LicenceHolder> pageIt(Pageable pageable, Example<LicenceHolder> e) {
		
		return licenceHolderService.pageIt(pageable, e);
	}
	
	

}
