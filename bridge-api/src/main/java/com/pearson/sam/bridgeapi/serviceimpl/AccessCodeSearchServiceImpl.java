/**
 * 
 */
package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.util.Utils.getQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch;
import com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService;
import com.pearson.sam.bridgeapi.repository.AccessCodeElasticSearchRepository;

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
public class AccessCodeSearchServiceImpl implements IAccessCodeSearchService {
	
	@Autowired
	AccessCodeElasticSearchRepository  accessCodeElasticSearchRepository;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService#search(org.springframework.data.domain.Pageable, java.lang.String)
	 */
	@Override
	public Page<AccessCodeSearch> search(Pageable pageable, String searchable) {
		return accessCodeElasticSearchRepository.search(queryStringQuery(searchable),pageable);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService#save(com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch)
	 */
	@Override
	public AccessCodeSearch save(AccessCodeSearch accessCodeSearch) {
		return accessCodeElasticSearchRepository.save(accessCodeSearch);
	}
	
	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService#saveAll(List<com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch>)
	 */
	@Override
	public List<AccessCodeSearch> saveAll(List<AccessCodeSearch> accessCodeSearchs) {
		return (List<AccessCodeSearch>) accessCodeElasticSearchRepository.saveAll(accessCodeSearchs);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService#update(com.pearson.sam.bridgeapi.elasticsearch.model.AccessCodeSearch)
	 */
	@Override
	public AccessCodeSearch update(AccessCodeSearch accessCodeSearch) {
		AccessCodeSearch accessCodeSearch1 = accessCodeElasticSearchRepository.findByAccessCode(accessCodeSearch.getAccessCode());
		
		if(Optional.ofNullable(accessCodeSearch1).isPresent()){
			accessCodeSearch.setId(accessCodeSearch1.getId());
		}
		
		if(!Optional.ofNullable(accessCodeSearch.getId()).isPresent())	{
			throw new BridgeApiGeneralException("Id doesn't exists!");
		}
		return accessCodeElasticSearchRepository.save(accessCodeSearch);
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.IAccessCodeSearchService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<AccessCodeSearch> pageIt(Pageable pageable, AccessCodeSearch searchable,StringMatcher sm) {
    return accessCodeElasticSearchRepository.search(getQueryBuilder(searchable,sm),pageable);
  }

}
