/**
 * 
 */
package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.util.Utils.getQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.iservice.IProductSearchService;
import com.pearson.sam.bridgeapi.repository.ProductElasticSearchRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author VGUDLSA
 *
 */
@Service
public class ProductSearchServiceImpl implements IProductSearchService {
	
	@Autowired
	ProductElasticSearchRepository productElasticSearchRepository;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IProductSearchService#search(org.springframework.data.domain.Pageable, java.lang.String)
	 */
	@Override
	public Page<ProductSearch> search(Pageable pageable, String searchable) {
		return productElasticSearchRepository.search(queryStringQuery(searchable),pageable);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IProductSearchService#save(com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch)
	 */
	@Override
	public ProductSearch save(ProductSearch productSearch) {
		return productElasticSearchRepository.save(productSearch);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IProductSearchService#update(com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch)
	 */
	@Override
	public ProductSearch update(ProductSearch productSearch) {
		
		ProductSearch productSearch1 = productElasticSearchRepository.findByProductId(productSearch.getProductId());
		if(Optional.ofNullable(productSearch1).isPresent()){
			productSearch.setId(productSearch1.getId());
		}

		if(!Optional.ofNullable(productSearch.getId()).isPresent())	{
			throw new BridgeApiGeneralException("Id doesn't exists!");
		}
		return productElasticSearchRepository.save(productSearch);
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IProductSearchService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.ProductSearch)
   */
  @Override
  public Page<ProductSearch> pageIt(Pageable pageable, ProductSearch searchable,StringMatcher sm) {
    return productElasticSearchRepository.search(getQueryBuilder(searchable,sm),pageable);
  }

}
