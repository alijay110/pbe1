/**
 * 
 */
package com.pearson.sam.bridgeapi.iservice;

import com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author VGUDLSA
 *
 */
public interface IProductSearchService {
	
	  public Page<ProductSearch> search(Pageable pageable, String searchable);
	  
	  public ProductSearch save(ProductSearch productSearch);
	  
	  public ProductSearch update(ProductSearch productSearch);

    /**
     * pageIt.
     * @param pageable
     * @param searchable
     * @return  
     */
    public Page<ProductSearch> pageIt(Pageable pageable, ProductSearch searchable,StringMatcher sm);

}
