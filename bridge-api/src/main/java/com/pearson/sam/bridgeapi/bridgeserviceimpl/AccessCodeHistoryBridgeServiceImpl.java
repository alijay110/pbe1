/**
 * 
 */
package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.ibridgeservice.IAccessCodeHistoryBridgeService;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeHistoryRepoService;
import com.pearson.sam.bridgeapi.model.AccessCodesHistory;
import com.pearson.sam.bridgeapi.model.User;
import com.pearson.sam.bridgeapi.security.ISessionFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author VGUDLSA
 *
 */
@Component
public class AccessCodeHistoryBridgeServiceImpl implements IAccessCodeHistoryBridgeService {
	
	@Autowired
	IAccessCodeHistoryRepoService accessCodeHistoryRepoService;
	
	@Autowired
	private ISessionFacade sessionFacade;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeHistoryBridgeService#create(com.pearson.sam.bridgeapi.model.AccessCodesHistory)
	 */
	@Override
	public AccessCodesHistory create(AccessCodesHistory accessCodesHistory) {
		User user = sessionFacade.getLoggedInUser(true);
		accessCodesHistory.setAlteredBy(user.getUid());
		return accessCodeHistoryRepoService.create(accessCodesHistory);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IAccessCodeHistoryBridgeService#pageIt(org.springframework.data.domain.Pageable, org.springframework.data.domain.Example)
	 */
	@Override
	public Page<AccessCodesHistory> pageIt(Pageable pageable, Example<AccessCodesHistory> e) {
		return accessCodeHistoryRepoService.pageIt(pageable, e);
	}

}
