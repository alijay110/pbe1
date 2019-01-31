package com.pearson.sam.bridgeapi.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.iservice.ILicenceHistoryService;
import com.pearson.sam.bridgeapi.model.LicenceHistory;
import com.pearson.sam.bridgeapi.repository.LicenceHistoryRespository;
import com.pearson.sam.bridgeapi.util.Utils;

@Service
public class LicenceHistoryServiceImpl implements ILicenceHistoryService {
	private static final Logger logger = LoggerFactory.getLogger(LicenceHistoryServiceImpl.class);

	@Autowired
	public LicenceHistoryRespository licenceHistoryRespository;

	@Override
	public LicenceHistory create(LicenceHistory licenceHistory) {
		// TODO Auto-generated method stub
		//logger.info("Create LicenceHistory {}", licenceHistory);
		licenceHistory.setAlterDate(Utils.isEmpty(licenceHistory.getAlterDate()) ? Utils.generateEpochDate()
				: licenceHistory.getAlterDate());
		return licenceHistoryRespository.save(licenceHistory);
	}

	@Override
	public Page<LicenceHistory> pageIt(Pageable pageable, Example<LicenceHistory> e) {
		// TODO Auto-generated method stub
		return licenceHistoryRespository.findAll(e, pageable);
	}

}
