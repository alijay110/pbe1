/**
 * 
 */
package com.pearson.sam.bridgeapi.ibridgeservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch;
import com.pearson.sam.bridgeapi.model.Voucher;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IVoucherBridgeService {
	
	Voucher create(Voucher voucher);
	
	List<Voucher> createMultiple(Voucher voucher);
	
	Voucher update(Voucher voucher);
	
	Voucher fetchOne(Voucher voucher);
	
	List<Voucher> getVoucherFromBatch(String batch);
	
	Page<Voucher> pageIt(Pageable pageable, Example<Voucher> e);

	Page<VoucherCodeSearch> search(Pageable pageable, String searchable);

  /**
   * pageIt.
   * @param pageable
   * @param query
   * @return  
   */
  Page<VoucherCodeSearch> pageIt(Pageable pageable, UserSearch query,StringMatcher sm);

}
