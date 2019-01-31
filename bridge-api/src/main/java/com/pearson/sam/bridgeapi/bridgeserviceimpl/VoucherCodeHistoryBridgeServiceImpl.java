/**
 * 
 */
package com.pearson.sam.bridgeapi.bridgeserviceimpl;

import com.pearson.sam.bridgeapi.ibridgeservice.IVoucherCodeHistoryBridgeService;
import com.pearson.sam.bridgeapi.iservice.IVoucherCodeHistoryService;
import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;

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
public class VoucherCodeHistoryBridgeServiceImpl implements IVoucherCodeHistoryBridgeService {
	
	@Autowired
	IVoucherCodeHistoryService voucherCodeHistoryService;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IVoucherCodeHistoryBridgeService#create(com.pearson.sam.bridgeapi.model.VoucherCodeHistory)
	 */
	@Override
	public VoucherCodeHistory create(VoucherCodeHistory voucherCodeHistory) {
		return voucherCodeHistoryService.create(voucherCodeHistory);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.Ibridgeservice.IVoucherCodeHistoryBridgeService#pageIt(org.springframework.data.domain.Pageable, org.springframework.data.domain.Example)
	 */
	@Override
	public Page<VoucherCodeHistory> pageIt(Pageable pageable, Example<VoucherCodeHistory> e) {
		return voucherCodeHistoryService.pageIt(pageable, e);
	}

}
