/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.model.VoucherCodeHistory;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IVoucherCodeHistoryService {
	
	VoucherCodeHistory create(VoucherCodeHistory voucherCodeHistory);
	
	Page<VoucherCodeHistory> pageIt(Pageable pageable, Example<VoucherCodeHistory> e);

}
