/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.model.Voucher;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IVoucherService {
	
	Voucher create(Voucher voucher);
	
	List<Voucher> createMultiple(Voucher voucher);
	
	Voucher update(Voucher voucher);
	
	Voucher fetchOne(Voucher voucher);
	
	List<Voucher> getVoucherFromBatch(String batch);
	
	Page<Voucher> pageIt(Pageable pageable, Example<Voucher> e);

  /**
   * getTotalCount.
   * @return  
   */
  Long getTotalCount();

}
