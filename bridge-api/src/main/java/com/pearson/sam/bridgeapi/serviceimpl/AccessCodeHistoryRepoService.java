package com.pearson.sam.bridgeapi.serviceimpl;

import com.pearson.sam.bridgeapi.iservice.IAccessCodeHistoryRepoService;
//import com.pearson.sam.bridgeapi.exceptions.ClientResponse;
import com.pearson.sam.bridgeapi.model.AccessCodesHistory;
import com.pearson.sam.bridgeapi.repository.AccessCodeHistoryRepository;
import com.pearson.sam.bridgeapi.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccessCodeHistoryRepoService implements IAccessCodeHistoryRepoService {
	private static final Logger logger = LoggerFactory.getLogger(AccessCodeHistoryRepoService.class);

	@Autowired
	private AccessCodeHistoryRepository accessCodeHistoryRepository;

	@Override
	public AccessCodesHistory create(AccessCodesHistory accessCodesHistory) {

		AccessCodesHistory accessCodeHistoryDB = accessCodeHistoryRepository.findTopByOrderByIdDesc();
		String accessCodeHistoryId = null;

		if (null != accessCodeHistoryDB && null != accessCodeHistoryDB.getAccessCodeHistoryId()
				&& accessCodeHistoryDB.getAccessCodeHistoryId().contains("PP2ACCESSCODEHISTORY")) {
			String[] numbers = accessCodeHistoryDB.getAccessCodeHistoryId().split("Y");
			int id = Integer.parseInt(numbers[1]) + 1;
			accessCodeHistoryId = "PP2ACCESSCODEHISTORY" + id;
		} else {
			accessCodeHistoryId = "PP2ACCESSCODEHISTORY00";
		}
		accessCodesHistory.setAccessCodeHistoryId(accessCodeHistoryId);
		if(Utils.isEmpty(accessCodesHistory.getAlterDate())) {
			accessCodesHistory.setAlterDate(Utils.generateEpochDate());
		}
		AccessCodesHistory resultACHistory = accessCodeHistoryRepository.save(accessCodesHistory);
		return resultACHistory;
	}

	@Override
	public Page<AccessCodesHistory> pageIt(Pageable pageable, Example<AccessCodesHistory> e) {
		return accessCodeHistoryRepository.findAll(e, pageable);
	}

}
