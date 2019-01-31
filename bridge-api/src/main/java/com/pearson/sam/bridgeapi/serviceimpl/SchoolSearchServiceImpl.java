/**
 * 
 */
package com.pearson.sam.bridgeapi.serviceimpl;

import static com.pearson.sam.bridgeapi.util.Utils.getQueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch;
import com.pearson.sam.bridgeapi.exceptions.BridgeApiGeneralException;
import com.pearson.sam.bridgeapi.iservice.ISchoolSearchService;
import com.pearson.sam.bridgeapi.repository.SchoolElasticSearchRepository;

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
public class SchoolSearchServiceImpl implements ISchoolSearchService {
	
	@Autowired
	SchoolElasticSearchRepository  schoolElasticSearchRepository;

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.ISchoolSearchService#search(org.springframework.data.domain.Pageable, java.lang.String)
	 */
	@Override
	public Page<SchoolSearch> search(Pageable pageable, String searchable) {
		return schoolElasticSearchRepository.search(queryStringQuery(searchable),pageable);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.ISchoolSearchService#save(com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch)
	 */
	@Override
	public SchoolSearch save(SchoolSearch schoolSearch) {
		return schoolElasticSearchRepository.save(schoolSearch);
	}

	/* (non-Javadoc)
	 * @see com.pearson.sam.bridgeapi.iservice.ISchoolSearchService#update(com.pearson.sam.bridgeapi.elasticsearch.model.SchoolSearch)
	 */
	@Override
	public SchoolSearch update(SchoolSearch schoolSearch) {
		SchoolSearch schoolSearch1 = schoolElasticSearchRepository.findBySchoolId(schoolSearch.getSchoolId());
		
		if(Optional.ofNullable(schoolSearch1).isPresent()){
			schoolSearch.setId(schoolSearch1.getId());
		}

		if(!Optional.ofNullable(schoolSearch.getId()).isPresent())	{
			throw new BridgeApiGeneralException("Id doesn't exists!");
		}
		return schoolElasticSearchRepository.save(schoolSearch);
	}

  /* (non-Javadoc)
   * @see com.pearson.sam.bridgeapi.iservice.ISchoolSearchService#pageIt(org.springframework.data.domain.Pageable, com.pearson.sam.bridgeapi.elasticsearch.model.UserSearch)
   */
  @Override
  public Page<SchoolSearch> pageIt(Pageable pageable, SchoolSearch searchable,StringMatcher sm) {
    return schoolElasticSearchRepository.search(getQueryBuilder(searchable,sm),pageable);
  }

}
