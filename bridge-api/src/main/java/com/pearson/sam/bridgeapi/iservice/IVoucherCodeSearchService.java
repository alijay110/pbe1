/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch;

import java.util.List;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IVoucherCodeSearchService {
	
	  public Page<VoucherCodeSearch> search(Pageable pageable, String searchable);
	  
	  public VoucherCodeSearch save(VoucherCodeSearch voucherCodeSearch);
	  
	  public VoucherCodeSearch update(VoucherCodeSearch voucherCodeSearch);

	  public void saveAll(List<VoucherCodeSearch> voucherCodeSearchObjects);

    /**
     * pageIt.
     * @param pageable
     * @param searchable
     * @return  
     */
    public Page<VoucherCodeSearch> pageIt(Pageable pageable, UserSearch searchable,StringMatcher sm);

}
