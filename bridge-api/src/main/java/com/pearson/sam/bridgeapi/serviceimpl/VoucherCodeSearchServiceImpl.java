/**
 * 
 */
package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.util.Utils.getQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.iservice.IVoucherCodeSearchService;
import com.pearson.sam.bridgeapi.repository.VoucherCodeElasticSearchRepository;

import java.util.List;
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
public class VoucherCodeSearchServiceImpl implements IVoucherCodeSearchService {
	
	@Autowired
	VoucherCodeElasticSearchRepository voucherCodeElasticSearchRepository;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IVoucherCodeSearchService#search(org.springframework.data.domain.Pageable, java.lang.String)
	 */
	@Override
	public Page<VoucherCodeSearch> search(Pageable pageable, String searchable) {
		return voucherCodeElasticSearchRepository.search(queryStringQuery(searchable),pageable);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IVoucherCodeSearchService#save(com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch)
	 */
	@Override
	public  VoucherCodeSearch save(VoucherCodeSearch voucherCodeSearch) {
		return voucherCodeElasticSearchRepository.save(voucherCodeSearch);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IVoucherCodeSearchService#update(com.pearson.sam.bridgeapi.elasticsearch.model.VoucherCodeSearch)
	 */
	@Override
	public VoucherCodeSearch update(VoucherCodeSearch voucherCodeSearch) {
		VoucherCodeSearch voucherCodeSearch1 = voucherCodeElasticSearchRepository.findByVoucherCode(voucherCodeSearch.getVoucherCode());
		
		if(Optional.ofNullable(voucherCodeSearch1).isPresent()){
			voucherCodeSearch.setId(voucherCodeSearch1.getId());
		}
		
		if(!Optional.ofNullable(voucherCodeSearch.getId()).isPresent())	{
			throw new BridgeApiGeneralException("Id doesn't exists!");
		}
		return voucherCodeElasticSearchRepository.save(voucherCodeSearch);
	}

	@Override
	public void saveAll(List<VoucherCodeSearch> voucherCodeSearchObjects) {
		voucherCodeElasticSearchRepository.saveAll(voucherCodeSearchObjects);
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IVoucherCodeSearchService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<VoucherCodeSearch> pageIt(Pageable pageable, UserSearch searchable,StringMatcher sm) {
    return voucherCodeElasticSearchRepository.search(getQueryBuilder(searchable,sm),pageable);
  }

}
