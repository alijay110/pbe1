package com.pearson.sam.bridgeapi.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pearson.sam.bridgeapi.iservice.IVoucherCodeHistoryService;
import com.pearson.sam.bridgeapi.model.SessionUser;
import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;
import com.pearson.sam.bridgeapi.repository.VoucherCodeHistoryRepository;
import com.pearson.sam.bridgeapi.security.ISessionFacade;
import com.pearson.sam.bridgeapi.util.Utils;

@Service
public class VoucherCodeHistoryService implements IVoucherCodeHistoryService {
	private static final Logger logger = LoggerFactory.getLogger(VoucherCodeHistoryService.class);

	@Autowired
	private VoucherCodeHistoryRepository voucherCodeHistoryRepository;
	
	@Autowired
	protected ISessionFacade sessionFacade;

	@Override
	public VoucherCodeHistory create(VoucherCodeHistory voucherCodeHistory) {
		//logger.info("Creating Voucher code History {}", voucherCodeHistory);

		VoucherCodeHistory voucherCodeHistoryDB = voucherCodeHistoryRepository.findTopByOrderByIdDesc();
		String voucherCodeHistoryId = null;

		if (null != voucherCodeHistoryDB && null != voucherCodeHistoryDB.getVoucherCodeHistoryId()
				&& voucherCodeHistoryDB.getVoucherCodeHistoryId().contains("PP2VOUCHERCODEHISTORY")) {
			String[] numbers = voucherCodeHistoryDB.getVoucherCodeHistoryId().split("Y");
			int id = Integer.parseInt(numbers[1]) + 1;
			voucherCodeHistoryId = "PP2VOUCHERCODEHISTORY" + id;
		} else {
			voucherCodeHistoryId = "PP2VOUCHERCODEHISTORY00";
		}

		if (null != voucherCodeHistory) {
			voucherCodeHistory.setVoucherCodeHistoryId(voucherCodeHistoryId);
			voucherCodeHistory.setAlterDate(Utils.generateEpochDate());
			voucherCodeHistory.setAlterBy(getUser(true).getUid());
			voucherCodeHistoryDB = voucherCodeHistoryRepository.save(voucherCodeHistory);
		}
		return voucherCodeHistoryDB;
	}

	@Override
	public Page<VoucherCodeHistory> pageIt(Pageable pageable, Example<VoucherCodeHistory> e) {
		return voucherCodeHistoryRepository.findAll(e, pageable);
	}
	
	public SessionUser getUser(boolean getLoggedInUser) {
		return sessionFacade.getLoggedInUser(getLoggedInUser);
	}

}
